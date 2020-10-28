const board = {
    initBoard: function () {
        this.showToBoardListButton();
    },
    showToBoardListButton: function () {
        const boardSearch = document.querySelector("#boardSearch").value;
    }
}

document.addEventListener("DOMContentLoaded", () => {
    board.initBoard();
});
