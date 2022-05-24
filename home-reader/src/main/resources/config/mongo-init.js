db.createUser(
    {
        user: "rootUser",
        pwd: "rootPassword",
        roles: [
            {
                role: "readWrite",
                db: "mongodb"
            }
        ]
    }
);