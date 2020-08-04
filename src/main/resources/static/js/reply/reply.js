const reply = {
    initReply: function () {
        this.requestReplyList(1, 10)
            .then(r => this.printReplyList(r));

        this.requestReplyPagination();

        // let createReplyButton = document.querySelector("#replyAddBtn");
        // createReplyButton.addEventListener("click", () => {
        //
        // });
    },
    requestReplyList: async function (page, size) {
        const boardId = document.querySelector("#boardId").value;
        const requestUrl = "/boards/" + boardId + "/replies?page=" + page + "&size=" + size + "sort=id,desc";
        return await ajax("GET", requestUrl);
    },
    requestReplyPagination: function (replyList) {
    },
    printReplyList: function (replyList) {
        const replyElements = replyList.content;
        replyElements.forEach(reply => {
            moment.locale('ko');
            reply.createdDateTime = moment(reply.createdDateTime, "YYYY-MM-DD`T`hh:mm").fromNow();
            reply.modifiedDateTime = moment(reply.modifiedDateTime, "YYYY-MM-DD`T`hh:mm").fromNow();
        });

        const replyElementTemplate = document.querySelector("#template").innerHTML;
        const template = Handlebars.compile(replyElementTemplate);
        const replyElement = template(replyElements);

        const replyListContainer = document.querySelector("#repliesDiv");
        replyListContainer.innerHTML += replyElement;
    },
    printPaging: function (replyList) {
        if (!replyList.first) {

        }

        if (!replyList.last) {

        }
    }
}

document.addEventListener("DOMContentLoaded", () => {
    reply.initReply();
});