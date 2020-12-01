$(function() {
    loadData()
});

function updateGamesView(data) {
    /*let htmlList = data.map(function (game) {
        return  '<li>'
            + new Date(game.created).toLocaleString() + ': '
            + game.gamePlayers.map(function(p) { return p.player.email}).join(', ')
            +'</li>';
    }).join('');*/
    let htmlGames =
            '<center> <table border="1" style="width:100%">'
            + '<thead> <th width="10%">ID</th>'
            + '<th width="20%">Start Time</th>'
            + '<th width="35%">Player 1</th>'
            + '<th width="35%">Player 2</th>'
            + '<tbody>';
    htmlGames += data.map(function (game) {
        return '<tr>'
             + '<td style="width:10%">' + game.id + '</td>'
             + '<td style="width:20%">' + new Date(game.created).toLocaleString() + '</td>'
             + game.gamePlayers.map(function(p) {
                    return '<td style="width:35%">' + p.player.email + '</td>';
                }).join('')
             +'</tr>';
    }).join('');
    htmlGames += '</tbody> </table> </center>';
    document.getElementById("games-list").innerHTML = htmlGames;
}
function updateLeaderboardView(data) {
    let htmlTable =
        '<center> <table border="2" style="width:100%">'
        + '<thead> <th width="55%">Player</th>'
        + '<th width="10%">W</th>'
        + '<th width="10%">D</th>'
        + '<th width="10%">L</th>'
        + '<th width="15%">Score</th> </thead>'
        + '<tbody>';

    htmlTable += data.map(function (player) {
        return '<tr>'
             + '<td>' + player.email + '</td>'
             + '<td>' + player.score.won + '</td>'
             + '<td>' + player.score.tied + '</td>'
             + '<td>' + player.score.lost + '</td>'
             + '<td>' + player.score.total + '</td>'
             +'</tr>';
    }).join('');
    htmlTable += '</tbody> </table> </center>';
    document.getElementById("leaderboard").innerHTML = htmlTable;
}

// load and display JSON sent by server for /games and /leaderboard

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