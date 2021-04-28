<span style="font-family: sans; font-size: 2em; font-weight: 600;">Multiuser Chat Application</span>

---

<span style="font-family: sans; font-size: 1.2em; font-weight: 500;">This is a multiuser chat application made by using java socket programming and swings. We can chat with any of the online person and also we can make chat groups & chat with other people who have joined the chatgroup. </span>

User & Server

1. User --> Server

   - login / logoff
   - status

2. Server --> User

   - online / offline of other users

3. User --> User
   - direct messages
   - broadcast // group messaging

Commands :

    login <user> <password>
    logoff
    msg <user> <text>
    guest: "msg jim Hello World" <-- Sent
    jim: "msg guest Hello World" <-- Receive

    #topic <--- chatroom / group chat
    join #topic
    send : msg #topic body
    recv: msg #topic:<login> body
