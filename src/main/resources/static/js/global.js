const ajax = async (method, url, data, property) => {
    const token = document.querySelector("#_csrf").content;

    let response;
    try {
        if(data == null) {
            response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': token
                },
            });
        } else {
            response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': token
                },
                body: JSON.stringify(data)
            });
        }
        const responseData = await response.json();

        if (property == null) {
            return responseData;
        }
        return responseData[property];
    } catch (e) {
        console.log("Error:", e);
    }
}

// document.addEventListener("DOMContentLoaded", () => {
//    initGlobalEvent();
// });