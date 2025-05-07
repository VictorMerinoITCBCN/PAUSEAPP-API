import requests
import dataset
from random import randint

BASE_URL = "http://localhost:8080"

def get_headers(token):
    return {
        'Authorization': f'Bearer {token}',
        'Content-Type': 'application/json'
    }

def login():
    response = requests.post(BASE_URL + "/auth/login", json={
        "email": "victor@gmail.com",
        "password": "super"
    })

    return response.json().get("token")

def createMedias(token):
    for media in dataset.medias:
        requests.post(BASE_URL + "/media", json={
            "type": "video",
            "url": media
        }, headers=get_headers(token))

def createTypes(token):
    for type in dataset.types:
        requests.post(BASE_URL + "/activity/types", json={
            "name": type
        }, headers=get_headers(token))

def createActivity(n, token):
    for i in range(n):
        activity = {
            "name": dataset.names[randint(0, len(dataset.names) -1)],
            "description": dataset.description,
            "typeId": randint(0, len(dataset.types) -1),
            "thumbnailURL": dataset.images[randint(0, len(dataset.images) -1)],
            "mediaId": randint(0, len(dataset.medias) -1),
            "isPremium": i%2==0
        }

        response = requests.post(BASE_URL + "/activity", json=activity, headers=get_headers(token))
        print(response)

token = login()
print(token)
# createMedias(token)
# createTypes(token)
createActivity(50, token)