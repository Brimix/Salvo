function victorySound() {
    let sound = new Howl({
        src: ['./sound/victory.mp3'],
        volume: 0.5
    })
    sound.play();
}

function sound(name) {
    let sound = new Howl({
        src: ['./sound/' + name + '.mp3'],
        volume: 0.5
    })
    sound.play();
}
