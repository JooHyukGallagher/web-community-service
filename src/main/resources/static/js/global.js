const ajax = async (method, url, data, property) => {
    let response;
    try {
        if(data == null) {
            response = await fetch(url, {
                method: method
            });
        } else {
            response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json'
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