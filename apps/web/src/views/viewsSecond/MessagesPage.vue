<template>
  <div class="page">
    <div class="wrapper">
      <div class="page-head">
        <span class="section-tag">MESSAGES</span>
        <h1 class="section-title">私信</h1>
        <p class="section-desc">与快船球迷一对一交流</p>
      </div>

      <div v-if="!isLoggedIn" class="login-tip card">
        <ChatDotRound class="tip-icon" />
        <p>登录后即可查看和发送私信</p>
        <router-link to="/login" class="btn btn-primary">去登录</router-link>
      </div>

      <div v-else class="chat-layout card">
        <!-- 会话列表 -->
        <aside class="conv-list" :class="{ hidden: activeConv }">
          <div class="conv-head">
            <span>会话</span>
            <span class="conv-count">{{ conversations.length }}</span>
          </div>

          <div class="conv-scroll">
            <div
              v-for="c in conversations"
              :key="c.userId"
              class="conv-item"
              :class="{ active: activeConv && String(activeConv.userId) === String(c.userId) }"
              @click="openConversation(c)"
            >
              <div class="conv-avatar">{{ avatarLetter(c.username) }}</div>
              <div class="conv-meta">
                <div class="conv-top">
                  <span class="conv-name">{{ c.username }}</span>
                  <span class="conv-time">{{ shortTime(c.lastTime) }}</span>
                </div>
                <div class="conv-bottom">
                  <span class="conv-last">{{ c.lastMessage }}</span>
                  <span v-if="c.unread" class="unread-badge">{{ c.unread > 99 ? '99+' : c.unread }}</span>
                </div>
              </div>
            </div>
            <el-empty v-if="!conversations.length" description="暂无会话" :image-size="70" />
          </div>
        </aside>

        <!-- 聊天窗口 -->
        <section class="chat-window">
          <template v-if="activeConv">
            <header class="chat-head">
              <button class="back-btn" @click="activeConv = null" title="返回会话列表">
                <ArrowLeft />
              </button>
              <div class="partner-avatar">{{ avatarLetter(activeConv.username) }}</div>
              <div class="partner-meta">
                <span class="partner-name">{{ activeConv.username }}</span>
                <router-link class="partner-link" :to="`/user/${activeConv.userId}`">查看主页</router-link>
              </div>
            </header>

            <div class="msg-scroll" ref="msgScroll">
              <div
                v-for="m in messages"
                :key="m.id"
                class="msg-row"
                :class="isMine(m) ? 'mine' : 'theirs'"
              >
                <div class="bubble" :class="isMine(m) ? 'bubble-mine' : 'bubble-theirs'">
                  {{ m.content }}
                </div>
                <span class="msg-time">{{ fullTime(m.createdAt) }}</span>
              </div>
              <el-empty v-if="!messages.length" description="发条消息打个招呼吧" :image-size="60" />
            </div>

            <footer class="chat-input">
              <el-input
                v-model="draft"
                placeholder="输入消息，按 Enter 发送..."
                @keyup.enter="send"
              />
              <button class="btn btn-primary send-btn" @click="send">
                <Promotion /> 发送
              </button>
            </footer>
          </template>

          <div v-else class="chat-placeholder">
            <ChatDotRound class="placeholder-icon" />
            <p>选择一个会话开始聊天</p>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/store/modules/user'
import { getConversations, getMessages, sendMessage, getUserProfile } from '@/api'

const route = useRoute()
const userStore = useUserStore()

const isLoggedIn = computed(() => !!userStore.isLoggedIn)
const myId = computed(() => userStore.userInfo?.id || userStore.userInfo?.userId)

const conversations = ref([])
const activeConv = ref(null)
const messages = ref([])
const draft = ref('')
const msgScroll = ref()
let pollTimer = null

onMounted(async () => {
  await loadConversations()
  const to = route.query.to
  if (to) await openUser(Number(to))
  // 每隔几秒刷新会话与当前聊天，带来实时收发体验
  pollTimer = setInterval(refresh, 4000)
})

onUnmounted(() => {
  if (pollTimer) clearInterval(pollTimer)
})

// 轮询刷新：更新会话列表（未读/最新消息），并同步当前会话的新消息
const refresh = async () => {
  await loadConversations()
  if (activeConv.value) {
    try {
      const list = (await getMessages(activeConv.value.userId)) || []
      if (list.length !== messages.value.length) {
        messages.value = list
        await nextTick()
        scrollToBottom()
      }
    } catch (e) {
      // 忽略轮询错误
    }
  }
}

const loadConversations = async () => {
  try {
    conversations.value = (await getConversations()) || []
  } catch (e) {
    conversations.value = []
  }
}

const openUser = async (userId) => {
  let conv = conversations.value.find((c) => String(c.userId) === String(userId))
  if (!conv) {
    try {
      const p = await getUserProfile(userId)
      if (p) {
        conv = { userId, username: p.username, avatar: p.avatar, lastMessage: '', lastTime: null, unread: 0 }
      }
    } catch (e) {
      // 拉不到资料时忽略
    }
  }
  if (conv) openConversation(conv)
}

const openConversation = async (conv) => {
  activeConv.value = conv
  try {
    messages.value = (await getMessages(conv.userId)) || []
  } catch (e) {
    messages.value = []
  }
  const item = conversations.value.find((c) => String(c.userId) === String(conv.userId))
  if (item) item.unread = 0
  await nextTick()
  scrollToBottom()
}

const send = async () => {
  const text = draft.value.trim()
  if (!text || !activeConv.value) return
  try {
    const m = await sendMessage({ receiverId: activeConv.value.userId, content: text })
    messages.value.push(m)
    draft.value = ''
    const item = conversations.value.find((c) => String(c.userId) === String(activeConv.value.userId))
    if (item) {
      item.lastMessage = text
      item.lastTime = m.createdAt
    } else {
      conversations.value.push({
        userId: activeConv.value.userId,
        username: activeConv.value.username,
        avatar: activeConv.value.avatar,
        lastMessage: text,
        lastTime: m.createdAt,
        unread: 0
      })
    }
    // 按最近消息时间倒序，让刚聊过的会话置顶
    conversations.value.sort((a, b) => String(b.lastTime || '').localeCompare(String(a.lastTime || '')))
    await nextTick()
    scrollToBottom()
  } catch (e) {
    // 错误已提示
  }
}

const isMine = (m) => String(m.senderId) === String(myId.value)

const avatarLetter = (name) => (name || '球').trim().charAt(0).toUpperCase()

const fmt = (t) => {
  if (!t) return ''
  return String(t).replace('T', ' ').slice(0, 16)
}

const shortTime = (t) => {
  const s = fmt(t)
  return s ? s.slice(5, 16) : ''
}

const fullTime = (t) => {
  const s = fmt(t)
  return s ? s.slice(5, 16) : ''
}

const scrollToBottom = () => {
  if (msgScroll.value) msgScroll.value.scrollTop = msgScroll.value.scrollHeight
}
</script>

<style scoped>
.page {
  padding: 48px 0 88px;
}

.page-head {
  text-align: center;
  margin-bottom: 36px;
}

.login-tip {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 14px;
  padding: 56px 24px;
  text-align: center;
  color: var(--text-muted);
}

.tip-icon {
  font-size: 40px;
  color: var(--text-faint);
}

/* ============ 布局 ============ */
.chat-layout {
  display: grid;
  grid-template-columns: 300px 1fr;
  min-height: 560px;
  overflow: hidden;
}

/* ============ 会话列表 ============ */
.conv-list {
  border-right: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.conv-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 18px 20px;
  font-family: var(--font-display);
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
  border-bottom: 1px solid var(--border);
}

.conv-count {
  font-size: 12px;
  font-weight: 500;
  color: var(--text-faint);
}

.conv-scroll {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}

.conv-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 14px;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: background var(--transition);
}

.conv-item:hover {
  background: var(--bg-hover);
}

.conv-item.active {
  background: rgba(200, 16, 46, 0.1);
}

.conv-avatar {
  width: 44px;
  height: 44px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--gradient-brand-blue);
  border-radius: 50%;
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 700;
  color: #fff;
}

.conv-meta {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.conv-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}

.conv-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.conv-time {
  font-size: 11px;
  color: var(--text-faint);
  flex-shrink: 0;
}

.conv-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}

.conv-last {
  font-size: 13px;
  color: var(--text-muted);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.unread-badge {
  min-width: 18px;
  height: 18px;
  padding: 0 5px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  background: var(--accent);
  border-radius: 9px;
  font-size: 11px;
  font-weight: 600;
  color: #fff;
  flex-shrink: 0;
}

/* ============ 聊天窗口 ============ */
.chat-window {
  display: flex;
  flex-direction: column;
  min-width: 0;
  min-height: 560px;
}

.chat-head {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  border-bottom: 1px solid var(--border);
}

.back-btn {
  display: none;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  background: transparent;
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  color: var(--text-secondary);
  cursor: pointer;
}

.back-btn:hover {
  color: var(--text-primary);
  border-color: var(--border-strong);
}

.partner-avatar {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--gradient-brand-blue);
  border-radius: 50%;
  font-family: var(--font-display);
  font-size: 16px;
  font-weight: 700;
  color: #fff;
}

.partner-meta {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 14px;
  min-width: 0;
}

.partner-name {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
}

.partner-link {
  font-size: 12px;
  color: var(--accent);
  text-decoration: none;
}

.partner-link:hover {
  text-decoration: underline;
}

.msg-scroll {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.msg-row {
  display: flex;
  flex-direction: column;
  gap: 4px;
  max-width: 70%;
}

.msg-row.mine {
  align-self: flex-end;
  align-items: flex-end;
}

.msg-row.theirs {
  align-self: flex-start;
  align-items: flex-start;
}

.bubble {
  padding: 10px 14px;
  border-radius: 14px;
  font-size: 14px;
  line-height: 1.6;
  word-break: break-word;
}

.bubble-mine {
  background: var(--accent);
  color: #fff;
  border-bottom-right-radius: 4px;
}

.bubble-theirs {
  background: var(--bg-hover);
  border: 1px solid var(--border);
  color: var(--text-primary);
  border-bottom-left-radius: 4px;
}

.msg-time {
  font-size: 11px;
  color: var(--text-faint);
}

.chat-input {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 20px;
  border-top: 1px solid var(--border);
}

.chat-input .el-input {
  flex: 1;
}

.send-btn {
  padding: 10px 22px;
  flex-shrink: 0;
}

.chat-placeholder {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  color: var(--text-muted);
}

.placeholder-icon {
  font-size: 48px;
  color: var(--text-faint);
}

/* ============ 响应式 ============ */
@media (max-width: 800px) {
  .chat-layout {
    grid-template-columns: 1fr;
  }

  .conv-list {
    border-right: none;
  }

  .conv-list.hidden {
    display: none;
  }

  .back-btn {
    display: inline-flex;
  }
}
</style>