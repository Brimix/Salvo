$(function() {
    loadData()
});

function updateGamesView(data) {
    let htmlList = data.map(function (game) {
        return  '<li>' + new Date(game.created).toLocaleString() + ': ' + game.gamePlayers.map(function(p) { return p.player.email}).join(', ')  +'</li>';
    }).join('');
  document.getElementById("games-list").innerHTML = htmlList;
}
function updateLeaderboardView(data) {
    //let htmlList = data.map(function (game) {
      //  return  '<li>' + new Date(game.created).toLocaleString() + ': ' + game.gamePlayers.map(function(p) { return p.player.email}).join(', ')  +'</li>';
    //}).join('');
  document.getElementById("leaderboard").innerHTML = htmlList;
}

// load and display JSON sent by server for /games

function loadData() {
    $.get("/api/games")
        .done(function(data) {
          updateGamesView(data);
        })
        .fail(function( jqXHR, textStatus ) {
          alert( "Failed: " + textStatus );
        });
    $.get("/api/leaderboard")
        .done(function(data) {
          updateLeaderboardView(data);
        })
        .fail(function( jqXHR, textStatus ) {
          alert( "Failed: " + textStatus );
        });
}