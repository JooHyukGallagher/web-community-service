// const ajax = async (method, url, data, property) => {
//     const token = document.querySelector("#_csrf").content;
//
//     let response;
//     try {
//         if (data == null) {
//             response = await fetch(url, {
//                 method: method,
//                 headers: {
//                     'Content-Type': 'application/json',
//                     'X-CSRF-TOKEN': token
//                 },
//             });
//         } else {
//             response = await fetch(url, {
//                 method: method,
//                 headers: {
//                     'Content-Type': 'application/json',
//                     'X-CSRF-TOKEN': token
//                 },
//                 body: JSON.stringify(data)
//             });
//         }
//         const responseData = await response.json();
//
//         if (property == null) {
//             return responseData;
//         }
//         return responseData[property];
//     } catch (e) {
//         console.log("Error:", e);
//     }
// }

const ajax = async (method, url, data, property) => {
    const token = document.querySelector("#_csrf").content;
    const init = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': token
        }
    }
    if (data !== null) {
        init.body = JSON.stringify(data);
    }

    let response;
    try {
        response = await fetch(url, init);
        const responseData = response.json();
        if (property == null) {
            return responseData;
        }
        return responseData[property];
    } catch (e) {
        console.log("Error:", e);
    }
}
