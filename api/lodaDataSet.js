const dataset = require("./dataset.json");
const { API } = require("./API.js");

const login = async () => {
    return await API.POST("/auth/login", {
        email: "victor@gmail.com",
        password: "super"
    });
}

const createMedias = (token) => {
    dataset.medias.forEach(async media => {
        await API.POST("/media", {
            type: media.type,
            url: media.url
        }, token);
    });
}

const createActivityTypes = (token) => {
    dataset.activityTypes.forEach(async aType => {
        await API.POST("/activity/types", {
            name : aType.name
        }, token);
    });
}

const createActivities = async (token) => {
    try {
        await Promise.all(dataset.activities.map(activity => 
            API.POST("/activity", {
                name: activity.name,
                description: activity.description,
                typeId: activity.typeId,
                thumbnailURL: activity.thumbnailURL,
                mediaId: activity.mediaId
            }, token)
        ));
    } catch (error) {
        console.error("Failed to create activities:", error);
    }
};


const main = async () => {
    const { token } = await login();
    console.log(token);
    createMedias(token);
    createActivityTypes(token);
    createActivities(token);
}

main();
