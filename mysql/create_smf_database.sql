-- generator script for SMF MySQL database, should be run in ssh from an ITU server

DROP DATABASE IF EXISTS smf;

CREATE DATABASE IF NOT EXISTS smf;

USE smf;

SELECT 'Creating tables ...' AS 'Print_Hack';

DROP TABLE IF EXISTS Users, Posts, TextPosts, PicturePosts, Pictures;

CREATE TABLE Users (
    userID INT NOT NULL AUTO_INCREMENT,
    userName VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL,
    country VARCHAR(100) NOT NULL,
    birthYear int NOT NULL,
    UNIQUE (userName),
    UNIQUE (email),
    PRIMARY KEY (userID)
);

CREATE TABLE Posts(
    postID INT NOT NULL AUTO_INCREMENT,
    userID INT NOT NULL,
    timePosted TIMESTAMP, -- should default to setting the current timestamp
    PRIMARY KEY (postID),
    FOREIGN KEY (userID) REFERENCES Users (userID)
);

CREATE TABLE TextPosts(
    postID INT NOT NULL,
    postText VARCHAR(145),
    FOREIGN KEY (postID) REFERENCES Posts (postID)
);

CREATE TABLE PicturePosts(
    postID INT NOT NULL,
    pictureID INT NOT NULL,
    FOREIGN KEY (postID) REFERENCES Posts (postID)
);

CREATE TABLE Pictures(
    pictureID INT NOT NULL AUTO_INCREMENT,
    picture BLOB  NOT NULL,
    PRIMARY KEY (pictureID)
);