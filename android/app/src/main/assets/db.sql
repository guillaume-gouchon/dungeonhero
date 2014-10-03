DROP TABLE IF EXISTS "android_metadata";
CREATE TABLE "android_metadata" ("locale" TEXT DEFAULT 'en_US');
INSERT INTO "android_metadata" VALUES ('en_US');
DROP TABLE IF EXISTS "game";
CREATE TABLE "game" (_id INTEGER PRIMARY KEY, "book" BLOB, "chapter" BLOB, "player" BLOB, "dungeon" BLOB);
