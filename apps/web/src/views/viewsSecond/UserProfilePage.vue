<template>
  <div class="page">
    <div class="wrapper">
      <!-- 用户卡片 -->
      <div class="profile-card card" v-if="profile">
        <div class="profile-head">
          <div class="profile-avatar">
            <img v-if="profile.avatar" :src="profile.avatar" :alt="profile.username" />
            <span v-else>{{ avatarLetter(profile.username) }}</span>
          </div>
          <div class="profile-meta">
            <div class="name-row">
              <h1 class="profile-name">{{ profile.username }}</h1>
              <span class="level-badge">{{ profile.level || '银卡会员' }}</span>
              <span v-if="isSelf" class="self-badge">我</span>
            </div>
            <p class="profile-bio">{{ profile.bio || '这个人很懒，还没有写简介。' }}</p>
            <p class="profile-join">加入于 {{ joinDate }}</p>
          </div>
          <div class="profile-actions">
            <router-link v-if="isSelf" to="/member-center/profile" class="btn btn-outline">编辑资料</router-link>
            <button v-else class="btn btn-primary" @click="goMessage">发私信</button>
          </div>
        </div>

        <div class="profile-stats">
          <div class="p-stat">
            <span class="p-value">{{ profile.postCount ?? 0 }}</span>
            <span class="p-label">发帖</span>
          </div>
          <div class="p-stat">
            <span class="p-value">{{ profile.likeCount ?? 0 }}</span>
            <span class="p-label">获赞</span>
          </div>
          <div class="p-stat">
            <span class="p-value">{{ profile.points ?? 0 }}</span>
            <span class="p-label">积分</span>
          </div>
          <div class="p-stat">
            <span class="p-value">{{ (profile.level === '金卡会员' ? '金卡' : '银卡') }}</span>
            <span class="p-label">会员等级</span>
          </div>
        </div>
      </div>

      <div class="profile-empty card" v-else>
        <p>用户不存在或已注销</p>
      </div>

      <!-- TA 的帖子 -->
      <div class="posts-section" v-if="profile">
        <div class="section-header">
          <h2 class="section-title">{{ isSelf ? '我的帖子' : 'TA 的帖子' }}</h2>
        </div>

        <div class="posts-list" v-if="posts.length">
          <article class="post-card card" v-for="post in posts" :key="post.id">
            <div class="post-header">
              <span class="post-tag" :class="post.tagClass">{{ post.tag }}</span>
              <span class="post-time">{{ post.time }}</span>
            </div>
            <h3 class="post-title">{{ post.title }}</h3>
            <p class="post-content">{{ post.content }}</p>
            <div class="post-stats">
              <span>赞 {{ post.likes }}</span>
              <span>评论 {{ post.comments }}</span>
              <span>浏览 {{ post.views }}</span>
            </div>
          </article>
        </div>
        <div class="empty-state card" v-else>
          <p>还没有发布过帖子</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/modules/user'
import { getUserProfile, getUserPosts } from '@/api'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const profile = ref(null)
const posts = ref([])

const myId = computed(() => userStore.userInfo?.id || userStore.userInfo?.userId)
const isSelf = computed(() => profile.value && myId.value && String(profile.value.id) === String(myId.value))

const userId = computed(() => route.params.id)

const joinDate = computed(() => {
  if (!profile.value || !profile.value.createdAt) return '—'
  return String(profile.value.createdAt).slice(0, 10)
})

const avatarLetter = (name) => (name || '球').trim().charAt(0).toUpperCase()

const goMessage = () => {
  router.push(`/messages?to=${userId.value}`)
}

onMounted(async () => {
  try {
    profile.value = await getUserProfile(userId.value)
  } catch (e) {
    profile.value = null
  }
  try {
    posts.value = (await getUserPosts(userId.value)) || []
  } catch (e) {
    posts.value = []
  }
})
</script>

<style scoped>
.page {
  padding: 48px 0 88px;
}

.profile-card {
  padding: 32px;
}

.profile-head {
  display: flex;
  align-items: center;
  gap: 24px;
}

.profile-avatar {
  width: 96px;
  height: 96px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  overflow: hidden;
  background: var(--gradient-brand-blue);
  font-family: var(--font-display);
  font-size: 40px;
  font-weight: 700;
  color: #fff;
}

.profile-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-meta {
  flex: 1;
  min-width: 0;
}

.name-row {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.profile-name {
  font-family: var(--font-display);
  font-size: 28px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.level-badge {
  padding: 4px 12px;
  background: rgba(200, 16, 46, 0.12);
  border: 1px solid rgba(200, 16, 46, 0.3);
  border-radius: var(--radius-full);
  font-size: 12px;
  color: var(--accent);
}

.self-badge {
  padding: 4px 10px;
  background: var(--bg-hover);
  border: 1px solid var(--border);
  border-radius: var(--radius-full);
  font-size: 12px;
  color: var(--text-muted);
}

.profile-bio {
  font-size: 15px;
  color: var(--text-secondary);
  margin: 10px 0 6px;
  line-height: 1.6;
}

.profile-join {
  font-size: 13px;
  color: var(--text-faint);
  margin: 0;
}

.profile-actions {
  flex-shrink: 0;
}

.profile-stats {
  display: flex;
  gap: 40px;
  margin-top: 28px;
  padding-top: 24px;
  border-top: 1px solid var(--border);
}

.p-stat {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.p-value {
  font-family: var(--font-display);
  font-size: 28px;
  font-weight: 700;
  color: var(--text-primary);
}

.p-label {
  font-size: 13px;
  color: var(--text-muted);
}

.profile-empty {
  padding: 60px;
  text-align: center;
  color: var(--text-muted);
}

.posts-section {
  margin-top: 40px;
}

.section-header {
  text-align: left;
  margin-bottom: 20px;
}

.section-title {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.posts-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.post-card {
  padding: 24px;
}

.post-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.post-tag {
  padding: 4px 12px;
  border-radius: var(--radius-full);
  font-size: 12px;
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

.post-time {
  font-size: 12px;
  color: var(--text-faint);
}

.post-title {
  font-size: 17px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 8px;
}

.post-content {
  font-size: 14px;
  color: var(--text-muted);
  line-height: 1.7;
  margin: 0 0 14px;
}

.post-stats {
  display: flex;
  gap: 18px;
  padding-top: 14px;
  border-top: 1px solid var(--border);
  font-size: 13px;
  color: var(--text-muted);
}

.empty-state {
  padding: 48px;
  text-align: center;
  color: var(--text-muted);
}

@media (max-width: 640px) {
  .profile-head {
    flex-direction: column;
    align-items: flex-start;
  }

  .profile-actions {
    width: 100%;
  }

  .profile-stats {
    gap: 24px;
  }
}
</style>