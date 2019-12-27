db = db.getSiblingDB('scores')

db.runCommand(
    {
        insert: "users",
        documents: [
            {
                _id: 1,
                userName: "user1",
                password: "$2a$10$5yjeLHuvOT1BYgJVucSmQ.i0hdidkpfTjy2NRBWX9avwiMt84Mqe6"
            }
        ]
    }
)