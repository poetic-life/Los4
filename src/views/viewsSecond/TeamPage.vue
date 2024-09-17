<template>
  <div id="team-page">
    <header class="header">
      <h1>Team Roster</h1>
    </header>
    <div class="main-content">
      <div class="player-list">
        <div v-for="player in players" :key="player.id" class="player-item">
          <img :src="player.image" alt="player image" class="player-image" />
          <div class="player-info">
            <h2 class="player-name">{{ player.name }}</h2>
            <p class="player-position">{{ player.position }}</p>
            <p class="player-description">{{ player.description }}</p>
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
      players: [
        {
          id: 1,
          name: 'John Doe',
          position: 'Point Guard',
          description: 'A skilled playmaker with exceptional court vision.',
          image: 'https://via.placeholder.com/150'
        },
        {
          id: 2,
          name: 'Jane Smith',
          position: 'Shooting Guard',
          description: 'An excellent shooter with great defensive skills.',
          image: 'https://via.placeholder.com/150'
        },
        {
          id: 3,
          name: 'James Brown',
          position: 'Small Forward',
          description: 'Versatile forward with a strong offensive game.',
          image: 'https://via.placeholder.com/150'
        },
        {
          id: 4,
          name: 'Emily Davis',
          position: 'Power Forward',
          description: 'Strong and aggressive, excels in rebounding and scoring in the paint.',
          image: 'https://via.placeholder.com/150'
        },
        {
          id: 5,
          name: 'Michael Johnson',
          position: 'Center',
          description: 'Dominant in the paint with excellent shot-blocking abilities.',
          image: 'https://via.placeholder.com/150'
        },
        {
          id: 6,
          name: 'Michael Jack',
          position: 'Center',
          description: 'Dominant in the paint with excellent shot-blocking abilities.',
          image: 'https://via.placeholder.com/150'
        }
      ]
    };
  }
};
</script>

<style scoped>
#team-page {
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
}

.player-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: space-around;
}

.player-item {
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 300px;
}

.player-image {
  flex: 0 0 150px;
  height: 150px;
  object-fit: cover;
}

.player-info {
  padding: 20px;
  flex: 1;
}

.player-name {
  font-size: 20px;
  margin: 0 0 10px;
}

.player-position {
  font-size: 16px;
  color: #007bff;
  margin: 0 0 10px;
}

.player-description {
  font-size: 14px;
  color: #555;
  margin: 0;
}
</style>
