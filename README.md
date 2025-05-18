Aplicación Web:
https://github.com/EnricUlloa/PROJ2_AppWeb/tree/victor_merino

Aplicación Android:
https://github.com/LoniousSosa/PauseAppMovil

# PAUSEAPP-API

# Endpoints

## Auth

Cada peticion a la api deberá incluir un token de autentificación en el header.

```js
headers: {
    Authorization: `Bearer ${token}`
}
```

Este token se obtiene con los siguientes endpoints:


`POST /auth/login`

```js
body: {
    email: "vicor@gmail.com",
    password: "password"
}
```

```js
response: {
    token: "xxxxx.yyyyy.zzzzz"
}
```

`POST /auth/register`

```js
body: {
    name: "victor",
    email: "victor@gmail.com",
    password: "password"
}
```

```js
response: {
    token: "xxxxx.yyyyy.zzzzz"
}
```

# User

## `GET /user/me`

*Devuelve datos del usuario correspondiente al token*

```js
response: {
    id: 1,
    name: "Victor",
    email: "victor@gmail.com",
    subscription: false,
    initialStressLevel: "stressed",
    actualStressLevel: "calmed",
    streakDays: 34,
    completedActivities: 60,
    alertInterval: 1740146391,
    recomendatedActivityTypes: [
        {
            id: 1,
            name: "meditation"
        }
    ]
}
```

## `GET /user?name={name}`

*Devuelve una lista de usuarios a partir del parametro name*

```js
response: {
    id: 1,
    name: "Victor",
    email: "victor@gmail.com",
    subscription: false,
    initialStressLevel: "stressed",
    actualStressLevel: "calmed",
    streakDays: 34,
    completedActivities: 60,
    alertInterval: 1740146391,
    recomendatedActivityTypes: [
        {
            id: 1,
            name: "meditation"
        }
    ]
}
```

## `PATCH /user/{id}`

*Modifica la propiedad del usuario del la id*

```js
body: {
    email: "victor@gmail.com",
}
```

## `GET /user/relations/sent/{id}`

*Devuelve la lista de relaciones enviadas*

```js
response: [
    {
        id: 1,
        status: "pending",
        user: {
            id: 1,
            name: "Victor",
            email: "victor@gmail.com",
            subscription: false,
            initialStressLevel: "stressed",
            actualStressLevel: "calmed",
            streakDays: 34,
            completedActivities: 60,
            alertInterval: 1740146391,
            recomendatedActivityTypes: [
                {
                    id: 1,
                    name: "meditation"
                }
            ]
        }
    }
]
```

## `GET /user/relations/received/{id}`

*Devuelve la lista de relaciones recividas*

```js
response: [
    {
        id: 1,
        status: "pending",
        user: {
            id: 1,
            name: "Victor",
            email: "victor@gmail.com",
            subscription: false,
            initialStressLevel: "stressed",
            actualStressLevel: "calmed",
            streakDays: 34,
            completedActivities: 60,
            alertInterval: 1740146391,
            recomendatedActivityTypes: [
                {
                    id: 1,
                    name: "meditation"
                }
            ]
        }
    }
]
```

## `POST /user/relations`

*Crea una solicitud de relación*

```js
body: {
    senderId: 34,
    receiverId: 28
}
```

## `PATCH /user/relations/{id}`

*Actualiza el estado de una relacion*

```js
body: {
    status: "accepted"
}
```

# Activity

## `GET /activity/{id}`

*Obtiene una actividad a partir del id*

```js
response: {
    id: 1,
    name: "Clase de relajación",
    description: "Clase de relajación guiada...",
    type: {
        id: 4,
        "meditation"
    },
    media: {
        id: 43,
        type: "video",
        url: "http:static.video/43"
    }
}
```

## `GET /activity/{name}`

*Obtiene una lista de actividades a partir del nombre*

```js
response: [
    name: "Clase de relajación",
    description: "Clase de relajación guiada...",
    type: {
        id: 4,
        name: "meditation"
    },
    media: {
        id: 43,
        type: "video",
        url: "http://static/video/43"
    }
]
```

## `GET /activity?typeIds={idList}`

*Obtiene una lista de actividades del tipo indicado en el parametro typeIds*

**idList es una lista de ids de tipos de actividad separados por comas. El param "?typeIds" es opcional.** 

**Ejemplo: "?typeIds=43,1,25,76"**

```js
response: [
    name: "Clase de relajación",
    description: "Clase de relajación guiada...",
    type: {
        id: 4,
        name: "meditation"
    },
    media: {
        id: 43,
        type: "video",
        url: "http://static/video/43"
    }
]
```

## `GET /activity/types`

*Obtiene una lista con los tipos de actividades disponibles*

```js
response: [
    {
        id: 3,
        name: "meditation"
    }
]
```

## `POST /activity`

*Crea una actividad*

```js
body: {
    name: "Clase de relajación",
    description: "Clase de relajación guiada...",
    type: {
        id: 4,
        name: "meditation"
    },
    media: {
        id: 43,
        type: "video",
        url: "http://static/video/43"
    }
}
```

## `POST /activity/types`

*Crea un tipo de actividad*

```js
body: {
    name: "meditation"
}
```

# User - Activity

## `GET /user/activity/{id}`

*Obtiene el registro de una actividad del usuario*

```js
response: {
    id: 54,
    time: 1740146391,
    status: true
}
```

# Alert

## `GET /alert`

*Obtiene una lista de alertas*

```js
response: [
    {
        id: 65,
        title: "Recordatorio meditación",
        message: "Llevas mucho tiempo sin meditar!",
    }
]
```

## `GET /alert/{id}`

*Obtiene una alerta a partir del id*

```js
response: {
    id: 34,
    title: "Recordatorio meditación",
    message: "Llevas mucho tiempo sin meditar!",
}
```

## `POST /alert`

```js
body: {
        title: "Recordatorio meditación",
        message: "Llevas mucho tiempo sin meditar!",
}
```
