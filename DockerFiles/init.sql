create database task_management;

\c task_management;

DROP TABLE IF EXISTS "comment";
DROP TABLE IF EXISTS "task";
DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS "role";

CREATE TABLE "role" (
	"id" SERIAL NOT NULL,
	"name" VARCHAR(255) NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE "user" (
	"id" SERIAL NOT NULL,
	"email" VARCHAR(255) NOT NULL,
	"password" VARCHAR(255) NOT NULL,
	"role_id" INTEGER NOT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fkdl9dqp078pc03g6kdnxmnlqpc" FOREIGN KEY ("role_id") REFERENCES "role" ("id") ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE "task" (
	"id" SERIAL NOT NULL,
	"description" VARCHAR(255) NOT NULL,
	"priority" SMALLINT NOT NULL,
	"status" SMALLINT NOT NULL,
	"title" VARCHAR(255) NOT NULL,
	"creator_id" INTEGER NOT NULL,
	"implementer_id" INTEGER NOT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fkgbm6entex11ihw1lb57wb1vgt" FOREIGN KEY ("creator_id") REFERENCES "user" ("id") ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT "fkjc2us2scobcbhrtvu85herc6o" FOREIGN KEY ("implementer_id") REFERENCES "user" ("id") ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT "task_priority_check" CHECK (((priority >= 0) AND (priority <= 2))),
	CONSTRAINT "task_status_check" CHECK (((status >= 0) AND (status <= 2)))
);

CREATE TABLE "comment" (
	"id" SERIAL NOT NULL,
	"value" VARCHAR(255) NOT NULL,
	"task_id" INTEGER NOT NULL,
	"user_id" INTEGER NOT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fkfknte4fhjhet3l1802m1yqa50" FOREIGN KEY ("task_id") REFERENCES "task" ("id") ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT "fkn6xssinlrtfnm61lwi0ywn71q" FOREIGN KEY ("user_id") REFERENCES "user" ("id") ON UPDATE CASCADE ON DELETE CASCADE
);
