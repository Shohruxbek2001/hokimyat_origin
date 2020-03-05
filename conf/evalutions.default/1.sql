CREATE TABLE "user"(
    "id" SERIAL NOT NULL PRIMARY KEY,
    "login" VARCHAR NOT NULL,
    "password" VARCHAR NOT NULL,
    "role" VARCHAR NOT NULL,
    UNIQUE("login")

);
CREATE TABLE "tadbir"(
    "id" SERIAL NOT NULL PRIMARY KEY,
    "hudud" VARCHAR NOT NULL,
    "rahbar" VARCHAR NOT NULL,
    "date" DATE NOT NULL,
    "text" VARCHAR NOT NULL,
    "value" VARCHAR NOT NULL,
    UNIQUE("hudud"),
    UNIQUE("rahbar"),
    UNIQUE("date")
);