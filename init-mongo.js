db.runCommand(
    {
        insert: "scores",
        documents: [ {
            userName: "user1",
            password: "$2a$10$xBq5u8YyQ1zMewSEu7SDD.fZ79x0YFOVyzTRfLOYi5S6M/PI6kSPG"
        }]
    }
)