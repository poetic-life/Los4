<template>
  <div id="news-page">
    <header class="header">
      <h1>NBA News</h1>
    </header>
    <div class="main-content">
      <div class="news-list">
        <div v-for="newsItem in newsItems" :key="newsItem.id" class="news-item">
          <img :src="newsItem.image" alt="news image" class="news-image" />
          <div class="news-content">
            <h2 class="news-title">{{ newsItem.title }}</h2>
            <p class="news-summary">{{ newsItem.summary }}</p>
            <a :href="newsItem.link" class="read-more">Read more</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      newsItems: [
        {
          id: 1,
          title: 'NBA Season Tips Off with a Bang',
          summary: 'The new NBA season has begun with exciting games and spectacular performances.',
          image: 'https://via.placeholder.com/300x200',
          link: '#'
        },
        {
          id: 2,
          title: 'Player X Sets New Record',
          summary: 'Player X sets a new record for points scored in a single game.',
          image: 'https://via.placeholder.com/300x200',
          link: '#'
        },
        {
          id: 3,
          title: 'Team Y Wins Championship',
          summary: 'Team Y wins the NBA Championship after a thrilling final series.',
          image: 'https://via.placeholder.com/300x200',
          link: '#'
        }
      ]
    };
  }
};
</script>

<style scoped>
#news-page {
  font-family: Arial, sans-serif;
  background-color: #f5f5f5;
  padding: 20px;
}

.header {
  background-color: #000;
  color: #fff;
  padding: 10px 20px;
  text-align: center;
}

.header h1 {
  margin: 0;
  font-size: 24px;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.news-item {
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.news-image {
  width: 300px;
  height: 200px;
  object-fit: cover;
}

.news-content {
  padding: 20px;
  flex: 1;
}

.news-title {
  font-size: 20px;
  margin: 0 0 10px;
}

.news-summary {
  font-size: 16px;
  color: #555;
  margin: 0 0 10px;
}

.read-more {
  color: #007bff;
  text-decoration: none;
  font-weight: bold;
}

.read-more:hover {
  text-decoration: underline;
}
</style>
