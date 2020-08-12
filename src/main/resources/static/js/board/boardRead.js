const board = {
    initBoard: function () {
        this.showToBoardListbutton();
    },
    showToBoardListbutton: function () {
        const boardSearch = document.querySelector("#boardSearch").value;
        console.log(boardSearch);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    board.initBoard();
});
