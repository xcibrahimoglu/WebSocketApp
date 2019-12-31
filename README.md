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
- P2P communication.
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

