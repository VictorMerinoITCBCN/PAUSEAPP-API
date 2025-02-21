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

## `GET /user`

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

## `PATCH /user`

*Modifica la propiedad del usuario que hacer referencia al token*

```js
body: {
    email: "victor@gmail.com",
}
```

## `GET /user/relations`

*Devuelve la lista de relaciones corespondiente al usuario del token*

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
    userId: 34
}
```

## `POST /user/relations/{id}`

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

## `GET /activity?types={idList}`

*Obtiene una lista de actividades del tipo indicado en el parametro types*

**idList es una lista de ids de tipos de actividad separados por comas. El param "?types" es opcional.** 

**Ejemplo: "?types=43,1,25,76"**

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
