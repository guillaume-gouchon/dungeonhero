DROP TABLE IF EXISTS "android_metadata";
CREATE TABLE "android_metadata" ("locale" TEXT DEFAULT 'en_US');
INSERT INTO "android_metadata" VALUES ('en_US');
DROP TABLE IF EXISTS "game";
CREATE TABLE "game" (_id INTEGER PRIMARY KEY, "book_id" INTEGER, "chapter" BLOB, "hero" BLOB, "dungeon" BLOB, "books_done" BLOB);
