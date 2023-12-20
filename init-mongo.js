db.createUser({
    user: "taskusername",
    pwd: "taskpassword",
    roles: [{ role: "readWrite", db: "task" }]
});