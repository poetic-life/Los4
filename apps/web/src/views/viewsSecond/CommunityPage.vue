<template>
  <div class="page">
    <div class="wrapper">
      <div class="page-head">
        <span class="section-tag">FAN COMMUNITY</span>
        <h1 class="section-title">球迷社区</h1>
        <p class="section-desc">一起聊聊你心中的快船</p>
      </div>

      <!-- 发帖栏 -->
      <div class="composer glass-card">
        <div class="composer-avatar">我</div>
        <el-input
          v-model="draft"
          class="composer-input"
          placeholder="分享你对快船的看法..."
          @keyup.enter="publish"
        />
        <button class="btn btn-primary composer-btn" @click="publish">
          <Promotion /> 发布
        </button>
      </div>

      <div class="community-content">
        <!-- 帖子列表 -->
        <div class="posts-list">
          <article class="post-card card" v-for="(post, index) in posts" :key="index">
            <div class="post-header">
              <router-link v-if="post.userId" :to="`/user/${post.userId}`" class="user-identity">
                <div class="user-avatar">{{ avatarLetter(post.username) }}</div>
                <div class="user-info">
                  <span class="username">{{ post.username }}</span>
                  <span class="post-time">{{ post.time }}</span>
                </div>
              </router-link>
              <div v-else class="user-identity">
                <div class="user-avatar">{{ avatarLetter(post.username) }}</div>
                <div class="user-info">
                  <span class="username">{{ post.username }}</span>
                  <span class="post-time">{{ post.time }}</span>
                </div>
              </div>
              <span class="post-tag" :class="post.tagClass">{{ post.tag }}</span>
            </div>
            <div class="post-body">
              <h3>{{ post.title }}</h3>
              <p>{{ post.content }}</p>
            </div>
            <div class="post-actions">
              <button class="action-btn" :class="{ liked: post.liked }" @click="toggleLike(index)">
                <Pointer /> 点赞 {{ post.likes }}
              </button>
              <button class="action-btn" @click="openComments(post)">
                <ChatDotRound /> 评论 {{ post.comments }}
              </button>
              <button class="action-btn"><View /> 浏览 {{ post.views }}</button>
            </div>
          </article>
        </div>

        <!-- 侧栏 -->
        <aside class="sidebar">
          <div class="sidebar-card card">
            <h3>热门话题</h3>
            <div class="topic-list">
              <a class="topic-item" v-for="(topic, index) in topics" :key="index" href="#">
                <span class="topic-rank" :class="'rank-' + (index + 1)">{{ index + 1 }}</span>
                <span class="topic-name">{{ topic.name }}</span>
                <span class="topic-count">{{ topic.count }}</span>
              </a>
            </div>
          </div>

          <div class="sidebar-card card">
            <h3>在线球迷</h3>
            <div class="online-panel">
              <span class="online-dot"></span>
              <span class="online-count">{{ onlineCount }}</span>
              <span class="online-label">位球迷正在线</span>
            </div>
          </div>
        </aside>
      </div>
    </div>

    <el-dialog v-model="commentVisible" title="评论" width="560px">
      <div v-if="comments.length" class="comment-list">
        <div class="comment-item" v-for="c in comments" :key="c.id">
          <div class="comment-head">
            <span class="comment-user">{{ c.username }}</span>
            <span class="comment-time">{{ formatTime(c.createdAt) }}</span>
          </div>
          <p class="comment-content">{{ c.content }}</p>
        </div>
      </div>
      <el-empty v-else description="还没有评论，来抢沙发" :image-size="60" />
      <div class="comment-input">
        <el-input v-model="commentDraft" type="textarea" :rows="2" placeholder="写下你的评论..." />
        <button class="btn btn-primary comment-send" @click="submitComment">发布评论</button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getPosts, createPost, likePost, getComments, addComment } from '@/api'
import { useUserStore } from '@/store/modules/user'

const router = useRouter()
const userStore = useUserStore()

// 发帖/点赞/评论都需要登录；未登录则提示并跳转登录页
const requireLogin = () => {
  if (userStore.isLoggedIn) return true
  ElMessage.warning('请先登录后再操作')
  router.push('/login')
  return false
}

const draft = ref('')

const posts = ref([])

const commentVisible = ref(false)
const currentPostId = ref(null)
const comments = ref([])
const commentDraft = ref('')

onMounted(async () => {
  await loadPosts()
})

const loadPosts = async () => {
  try {
    const data = (await getPosts()) || []
    posts.value = data.map((p) => ({ ...p, liked: false }))
  } catch (e) {
    // 加载失败时保持空列表
  }
}

const topics = ref([
  { name: '#哈登加盟快船#', count: '2.3万' },
  { name: '#伦纳德防守集锦#', count: '1.8万' },
  { name: '#威少三双记录#', count: '1.5万' },
  { name: '#快船总冠军#', count: '1.2万' },
  { name: '#乔治伤愈复出#', count: '9800' }
])

const onlineCount = ref('1,208')

const avatarLetter = (name) => (name || '').trim().charAt(0).toUpperCase()

const publish = async () => {
  if (!requireLogin()) return
  const text = draft.value.trim()
  if (!text) {
    ElMessage.warning('请输入内容')
    return
  }
  try {
    const post = await createPost({
      title: text.length > 24 ? text.slice(0, 24) + '…' : text,
      content: text,
      tag: '讨论',
      tagClass: 'discussion'
    })
    posts.value.unshift({ ...post, liked: false })
    draft.value = ''
    ElMessage.success('发布成功')
  } catch (e) {
    // 错误已提示
  }
}

const toggleLike = async (index) => {
  if (!requireLogin()) return
  const post = posts.value[index]
  if (!post.liked) {
    try {
      const likes = await likePost(post.id)
      post.likes = likes
      post.liked = true
    } catch (e) {
      // 错误已提示
    }
  }
}

const openComments = async (post) => {
  currentPostId.value = post.id
  commentVisible.value = true
  commentDraft.value = ''
  try {
    comments.value = (await getComments(post.id)) || []
  } catch (e) {
    comments.value = []
  }
}

const submitComment = async () => {
  if (!requireLogin()) return
  const text = commentDraft.value.trim()
  if (!text) {
    ElMessage.warning('请输入评论内容')
    return
  }
  try {
    const c = await addComment(currentPostId.value, { content: text })
    comments.value.push(c)
    commentDraft.value = ''
    const post = posts.value.find((p) => p.id === currentPostId.value)
    if (post) post.comments++
    ElMessage.success('评论成功')
  } catch (e) {
    // 错误已提示
  }
}

const formatTime = (t) => (t ? String(t).replace('T', ' ').slice(0, 16) : '')
</script>

<style scoped>
.page {
  padding: 48px 0 88px;
}

.page-head {
  text-align: center;
  margin-bottom: 36px;
}

/* ============ 发帖栏 ============ */
.composer {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 16px 20px;
  border-radius: var(--radius-lg);
  margin-bottom: 24px;
}

.composer-avatar {
  width: 44px;
  height: 44px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--gradient-brand);
  border-radius: 50%;
  font-size: 16px;
  font-weight: 700;
  color: #fff;
}

.composer-input {
  flex: 1;
}

.composer-btn {
  padding: 10px 24px;
  flex-shrink: 0;
}

/* ============ 主体布局 ============ */
.community-content {
  display: grid;
  grid-template-columns: 1fr 320px;
  gap: 24px;
  align-items: start;
}

.posts-list {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.post-card {
  padding: 24px;
}

/* ============ 帖子头部 ============ */
.post-header {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 16px;
}

.user-identity {
  flex: 1;
  min-width: 0;
  display: flex;
  align-items: center;
  gap: 14px;
  text-decoration: none;
}

.user-identity:hover .username {
  color: var(--accent);
}

.user-avatar {
  width: 46px;
  height: 46px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--gradient-brand-blue);
  border: 1px solid var(--border);
  border-radius: 50%;
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 700;
  color: #fff;
}

.user-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.username {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
}

.post-time {
  font-size: 12px;
  color: var(--text-faint);
}

.post-tag {
  padding: 5px 14px;
  border-radius: var(--radius-full);
  font-size: 12px;
  flex-shrink: 0;
}

.post-tag.discussion {
  background: rgba(200, 16, 46, 0.12);
  color: var(--accent);
}

.post-tag.news {
  background: rgba(29, 66, 138, 0.16);
  color: #6f9cff;
}

.post-tag.prediction {
  background: rgba(46, 204, 113, 0.12);
  color: #2ecc71;
}

/* ============ 帖子正文 ============ */
.post-body h3 {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 8px;
  line-height: 1.4;
}

.post-body p {
  font-size: 14px;
  color: var(--text-muted);
  line-height: 1.7;
  margin: 0;
}

/* ============ 帖子操作 ============ */
.post-actions {
  display: flex;
  gap: 10px;
  padding-top: 16px;
  margin-top: 16px;
  border-top: 1px solid var(--border);
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 7px 16px;
  background: transparent;
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  color: var(--text-muted);
  font-size: 13px;
  cursor: pointer;
  transition: all var(--transition);
}

.action-btn:hover {
  border-color: var(--border-strong);
  color: var(--text-primary);
}

.action-btn.liked {
  background: rgba(200, 16, 46, 0.12);
  border-color: var(--accent);
  color: var(--accent);
}

/* ============ 侧栏 ============ */
.sidebar {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.sidebar-card {
  padding: 24px;
}

.sidebar-card h3 {
  font-family: var(--font-display);
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 1px;
  color: var(--text-primary);
  margin: 0 0 18px;
}

.topic-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.topic-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px;
  background: var(--bg-hover);
  border-radius: var(--radius-md);
  transition: all var(--transition);
}

.topic-item:hover {
  background: var(--bg-card);
  border: 1px solid var(--border);
  transform: translateX(4px);
}

.topic-rank {
  width: 24px;
  height: 24px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-sm);
  font-size: 13px;
  font-weight: 700;
}

.rank-1 { background: #f5b301; color: #1a1a1a; }
.rank-2 { background: #a0a8b4; color: #1a1a1a; }
.rank-3 { background: #c97b3d; color: #fff; }
.rank-4, .rank-5 { background: var(--bg-card); color: var(--text-muted); }

.topic-name {
  flex: 1;
  font-size: 14px;
  color: var(--text-secondary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.topic-item:hover .topic-name {
  color: var(--accent);
}

.topic-count {
  font-size: 12px;
  color: var(--text-faint);
  flex-shrink: 0;
}

/* ============ 在线球迷 ============ */
.online-panel {
  display: flex;
  align-items: center;
  gap: 12px;
}

.online-dot {
  width: 12px;
  height: 12px;
  background: #2ecc71;
  border-radius: 50%;
  box-shadow: 0 0 0 5px rgba(46, 204, 113, 0.15);
  animation: pulse 1.6s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { box-shadow: 0 0 0 5px rgba(46, 204, 113, 0.16); }
  50% { box-shadow: 0 0 0 9px rgba(46, 204, 113, 0.06); }
}

.online-count {
  font-family: var(--font-display);
  font-size: 40px;
  font-weight: 700;
  line-height: 1;
  color: var(--text-primary);
}

.online-label {
  font-size: 13px;
  color: var(--text-muted);
}

/* ============ 评论弹窗 ============ */
.comment-list {
  max-height: 320px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 14px;
  margin-bottom: 16px;
}

.comment-item {
  padding: 12px 14px;
  background: var(--bg-hover);
  border-radius: var(--radius-md);
}

.comment-head {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 6px;
}

.comment-user {
  font-size: 13px;
  font-weight: 600;
  color: var(--accent);
}

.comment-time {
  font-size: 12px;
  color: var(--text-faint);
}

.comment-content {
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.6;
  margin: 0;
}

.comment-input {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.comment-send {
  align-self: flex-end;
  padding: 8px 20px;
}

/* ============ 响应式 ============ */
@media (max-width: 900px) {
  .community-content {
    grid-template-columns: 1fr;
  }

  .composer {
    flex-wrap: wrap;
  }

  .composer-input {
    order: 3;
    width: 100%;
    flex-basis: 100%;
  }
}
</style>