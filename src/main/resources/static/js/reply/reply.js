const reply = {
    isValid: false,
    initReply: function () {
        this.requestReplyList(0, 10).then(r => {
            this.printReplyList(r)
            this.printPaging(r, 10);
        });

        this.regPageClickEvent();
        this.regCreateReplyEvent();
        this.regNewReplyWriterValidate();
    },
    requestReplyList: async function (page, size) {
        if (size > 10000) {
            size = 10;
        }
        const boardId = document.querySelector("#boardId").value;
        const requestUrl = "/boards/" + boardId + "/replies?page=" + page + "&size=" + size + "sort=id,desc";
        return await ajax("GET", requestUrl);
    },
    printReplyList: function (replyList) {
        const replyElements = replyList.content;
        replyElements.forEach(reply => {
            moment.locale('ko');
            reply.createdDateTime = moment(reply.createdDateTime, "YYYY-MM-DD`T`hh:mm").fromNow();
            reply.modifiedDateTime = moment(reply.modifiedDateTime, "YYYY-MM-DD`T`hh:mm").fromNow();
        });

        const replyElementTemplate = document.querySelector("#template").innerHTML;
        const bindTemplate = Handlebars.compile(replyElementTemplate);
        const replyElement = bindTemplate(replyElements);

        const replyListContainer = document.querySelector("#repliesDiv");
        replyListContainer.innerHTML = replyElement;
    },
    printPaging: function (replyList) {
        const pageableInfo = replyList.pageable;

        let endPage = (Math.ceil(replyList.number / pageableInfo.pageSize) * pageableInfo.pageSize);
        let tempEndPage = replyList.totalPages;
        if (endPage === 0 || endPage > tempEndPage) {
            endPage = tempEndPage;
        }
        let startPage = (endPage - pageableInfo.pageSize) + 1;
        if (startPage < 0) {
            startPage = 1;
        }
        let prev = startPage !== 1;
        let next = endPage * replyList.size < replyList.totalElements;

        let str = "";

        if (prev) {
            str +=
                "<li class='page-item'>\n" +
                "   <a class='page-link' aria-label='Previous' href='" + (startPage - 1) + "'> << </a>\n" +
                "</li>"
        }

        for (let i = startPage, len = endPage; i <= len; i++) {
            const isActive = replyList.number === (i - 1) ? 'active' : '';
            str +=
                "<li class='page-item " + isActive + "'>\n" +
                "   <a class='page-link' href='" + (i - 1) + "'>" + i + "</a>\n" +
                "</li>"
        }

        if (next) {
            str +=
                "<li class='page-item'>\n" +
                "   <a class='page-link' aria-label='Next' href='" + (endPage + 1) + "'> >> </a>\n" +
                "</li>"
        }


        let paginationContainer = document.querySelector(".pagination");
        paginationContainer.innerHTML = str;
    },
    regPageClickEvent: function () {
        const pageButton = document.querySelector(".pagination");
        pageButton.addEventListener("click", (evt) => {
            evt.preventDefault();
            const requestPageNum = evt.target.text;
            this.requestReplyList(requestPageNum - 1, 10).then(r => {
                this.printReplyList(r);
                this.printPaging(r, 10);
            })
        });
    },
    regCreateReplyEvent: function () {
        const replyAddButton = document.querySelector("#replyAddBtn");
        replyAddButton.addEventListener("click", async (evt) => {
            const replyObj = document.querySelector("#newReplyWriter");
            const createReplyForm = {
                content: replyObj.value,
                boardWriterNickname: document.querySelector("#nickname").value
            }

            if(!this.isValid) {
                alert("글자수를 2자 이상 1000자 이하로 입력해주세요");
            } else {
                const boardId = document.querySelector("#boardId").value;
                const requestUrl = "/boards/" + boardId + "/replies";
                const replyReadForm = await ajax("POST", requestUrl, createReplyForm);
                alert("등록 되었습니다.");

                replyObj.value = "";

                this.requestReplyList(0, 10).then(r => {
                    this.printReplyList(r);
                    this.printPaging(r, 10);
                })
            }
        })
    },
    regNewReplyWriterValidate: function () {
        const newReplyWriter = document.querySelector("#newReplyWriter");
        newReplyWriter.addEventListener("keyup", (evt) => {
            let replyContent = evt.target.value;
            this.isValid = (/^.{2,1000}$/).test(replyContent);
        });
    }
}

document.addEventListener("DOMContentLoaded", () => {
    reply.initReply();
});