$(function() {
    loadData();
});

function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
};

function loadData(){
    $.get('/api/game_fullview/'+getParameterByName('gp'))
        .done(function(data) {
            let playerInfo;
            if(Object.keys(data.gamePlayers).length > 1){
                if(data.gamePlayers[0].id == getParameterByName('gp'))
                    playerInfo = [data.gamePlayers[0].player,data.gamePlayers[1].player];
                else
                    playerInfo = [data.gamePlayers[1].player,data.gamePlayers[0].player];
                $('#playerInfo').text(playerInfo[0].email + '(you) vs ' + playerInfo[1].email);
                $('#playerText').text('');
            }
            else{
                playerInfo = [data.gamePlayers[0].player];
                $('#playerInfo').text(playerInfo[0].email + "(you)");
                $('#playerText').text('You have no rival yet!');
            }
            data.ships.forEach(function(shipPiece){
                shipPiece.locations.forEach(function(shipLocation){
                    $('#'+shipLocation).addClass('ship-piece');
                })
            });

            data.salvoes.forEach(function(salvoPiece){
                if(salvoPiece.player == playerInfo[0].id){
                    salvoPiece.locations.forEach(function(salvoLocation){
                        $('#R'+salvoLocation).addClass('salvo');
                    })
                }
                else{
                    salvoPiece.locations.forEach(function(salvoLocation){
                        if($('#'+salvoLocation).hasClass('ship-piece')){
                            $('#'+salvoLocation).addClass('ship-piece-hit');
                        }
                        else{
                            $('#'+salvoLocation).addClass('salvo');
                        }
                    })
                }
            });

        })
        .fail(function( jqXHR, textStatus ) {
          alert( "Failed: " + textStatus );
        });
};