# Hey Buddy! 
This is a **chat application** that is used via only web browsers.

## Components
### Back-End Side
    - Java 8
    - Apache Tomcat Server
    - Java WebSocket
    - GSON Library as JSON Parser
### Front-End Side
    - JavaScript
    - HTML5
    - HTML5 WebSocket API
    - CSS
### Persistence Layer
    - Mongo DB (NoSQL DB)

## Features
- Authentication with JWT.
- One-to-one messaging.
- Each message and user persist with all required fields in Mongo DB.
- It supports two type message context: text and image.

## WebSocket Payloads

Messages from client to server can be two types: **message** or **connectedUser**.
JSON differs according to type.

If type is "message", a sample message is like:
```json 
{
    "type" : "message",
    "contentType" : "txt",
    "content" : "hey",
    "sender" : "Can",
    "receiver" : "Gizem",
    "receivedDate" : "2019-12-30 22:23"
}
```
If type is "connectedUser":
```json 
{
    "type" : "connectedUser",
    "username" : "Can"
}
```
## DB Collections

**Message**
```json 
{
    "_id": { "$oid" : "5e0a4e9265f14d04cbad6f8f" },
    "sender" : "Can",
    "receiver" : "Gizem",
    "contentType" : "txt",
    "content" : "hey",
    "receivedDate" : "2019-12-30 22:22"
}
```
**User**
```json 
{
    "_id" : { "$oid" : "5e0a4dad65f14d04cbad6f8c" },
    "name" : "can",
    "lastname" : "ibrahimoglu",
    "email" : "can190189@gmail.com",
    "username" : "Can",
    "password" : "123"
}
```
## Authentication Overview

In the first login for a client, as seen below image, if already an account access token is generated and the chat screen appears. 

<p align="center">
<img border-style="solid" width="600" alt="Ekran Resmi 2020-12-30 22 16 02" src="https://user-images.githubusercontent.com/28542558/71620472-472a2d00-2bdb-11ea-81b5-a67217e48af0.png">
 </p>
 <p align="center"> Image 1: Welcome Page</p>
 
If the user is new for Hey Buddy, the user is able to chat friends once signing up.

<p align="center">
<img  width="600" alt="Ekran Resmi 2020-12-30 22 16 29" src="https://user-images.githubusercontent.com/28542558/71620504-6628bf00-2bdb-11ea-9324-0a5eeedd51f8.png">
</p>
<p align="center"> Image 2: Sign Up Page</p>


## Conversation Panel

On the left side of the screen online users are displayed. When a message receives from a user, the notification appears in username area under the chats title.

<p align="center">
<img width="600" alt="Ekran Resmi 2020-12-30 22 36 51" src="https://user-images.githubusercontent.com/28542558/71620516-6f199080-2bdb-11ea-8bb2-bf3af59d0628.png">
</p>
<p align="center"> Image 3: Chat Page</p>
