-- generator script for SMF MySQL database, should be run in ssh from an ITU server

DROP DATABASE IF EXISTS smf;

CREATE DATABASE IF NOT EXISTS smf;

USE smf;

SELECT 'Creating tables ...' AS 'Print_Hack';

DROP TABLE IF EXISTS Countries, Users, Posts, TextPosts, PicturePosts, Likes, LikeRelationship;

CREATE TABLE Countries (
countryID INT NOT NULL AUTO_INCREMENT,
countryName VARCHAR(100) NOT NULL,
PRIMARY KEY (countryID),
UNIQUE (countryName))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE Users (
    userID INT NOT NULL AUTO_INCREMENT,
    userName VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL,
    countryID INT NOT NULL,
    birthYear int NOT NULL,
    UNIQUE (userName),
    UNIQUE (email),
    PRIMARY KEY (userID),
    CONSTRAINT `fk_Users_countryID`
      FOREIGN KEY (countryID)
      REFERENCES `smf`.`tempCountries` (`countryID`)
      ON DELETE NO ACTION
      ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


CREATE TABLE Posts(
    postID INT NOT NULL AUTO_INCREMENT,
    userID INT NOT NULL,
    postType INT NOT NULL,
    tStamp BIGINT SIGNED NOT NULL,
    universalTimeStamps VARCHAR(35),
    localTimeStamps VARCHAR(35),
    PRIMARY KEY (postID),
    CONSTRAINT `fk_Posts_userID`
      FOREIGN KEY (userID)
      REFERENCES `smf`.`tempUsers` (`userID`)
      ON DELETE CASCADE
      ON UPDATE NO ACTION)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


CREATE TABLE TextPosts(
    postID INT NOT NULL,
    postText VARCHAR(145),
    PRIMARY KEY (postID),
    CONSTRAINT `fk_TextPosts_postID`
      FOREIGN KEY (postID)
      REFERENCES `smf`.`tempPosts` (`postID`)
      ON DELETE CASCADE
      ON UPDATE NO ACTION)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8


CREATE TABLE PicturePosts(
    postID INT NOT NULL,
    picture BLOB NOT NULL,
    PRIMARY KEY (postID),
    CONSTRAINT `fk_PicturePosts_postID`
      FOREIGN KEY (postID)
      REFERENCES `smf`.`tempPosts` (`postID`)
      ON DELETE CASCADE
      ON UPDATE NO ACTION)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8


CREATE TABLE Likes(
    postID INT NOT NULL,
    likes INT UNSIGNED NOT NULL DEFAULT 0,
    PRIMARY KEY (postID),
    UNIQUE (postID),
    CONSTRAINT `fk_Likes_postID`
      FOREIGN KEY (postID)
      REFERENCES `smf`.`tempPosts` (`postID`)
      ON DELETE CASCADE
      ON UPDATE NO ACTION)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8


CREATE TABLE LikeRelationship(
    postID INT NOT NULL,
    userID INT NOT NULL,
    UNIQUE (postID,userID),
    CONSTRAINT `fk_LikeRelationship_postID`
      FOREIGN KEY (postID)
      REFERENCES `smf`.`tempPosts` (`postID`)
      ON DELETE CASCADE
      ON UPDATE NO ACTION,
    CONSTRAINT `fk_Likes_userID`
      FOREIGN KEY (userID)
      REFERENCES `smf`.`tempUsers` (`userID`)
      ON DELETE CASCADE
      ON UPDATE NO ACTION)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8





INSERT INTO Countries (countryName) VALUES
('Afghanistan'),('Albania'),('Algeria'),('Andorra'),
('Angola'),('Antigua & Barbuda'),('Argentina'),('Armenia'),
('Australia'),('Austria'),('Azerbaijan'),('Bahamas'),('Bahrain'),
('Bangladesh'),('Barbados'),('Belarus'),('Belgium'),('Belize'),
('Benin'),('Bhutan'),('Bolivia'),('Bosnia & Herzegovina'),
('Botswana'),('Brazil'),('Brunei'),('Bulgaria'),('Burkina Faso'),
('Burundi'),('Cabo Verde'),('Cambodia'),('Cameroon'),('Canada'),
('Central African Republic (CAR)'),('Chad'),('Chile'),('China'),
('Colombia'),('Comoros'),('Congo'), ('The Democratic Republic of the Congo'),
('Costa Rica'),("Cote d' Ivoire"),('Croatia'),('Cuba'),('Cyprus'),
('Czechia'),('Denmark'),('Djibouti'),('Dominica'),('Dominican Republic'),
('Ecuador'),('Egypt'),('El Salvador'),('Equatorial Guinea'),('Eritrea'),('Estonia'),
('Eswatini (Swaziland)'),('Ethiopia'),('Fiji'),('Finland'),('France'),('Gabon'),('Gambia'),
('Georgia'),('Germany'),('Ghana'),('Greece'),('Grenada'),('Guatemala'),('Guinea'),('Guinea-Bissau'),
('Guyana'),('Haiti'),('Honduras'),('Hungary'),('Iceland'),('India'),('Indonesia'),('Iran'),
('Iraq'),('Ireland'),('Israel'),('Italy'),('Jamaica'),('Japan'),('Jordan'),('Kazakhstan'),
('Kenya'),('Kiribati'),('Kosovo'),('Kuwait'),('Kyrgyzstan'),('Laos'),('Latvia'),('Lebanon'),('Lesotho'),
('Liberia'),('Libya'),('Liechtenstein'),('Lithuania'),('Luxembourg'),('Madagascar'),('Malawi'),
('Malaysia'),('Maldives'),('Mali'),('Malta'),('Marshall Islands'),('Mauritania'),('Mauritius'),
('Mexico'),('Micronesia'),('Moldova'),('Monaco'),('Mongolia'),('Montenegro'),('Morocco'),
('Mozambique'),('Myanmar (Burma)'),('Namibia'),('Nauru'),('Nepal'),('Netherlands'),('New Zealand'),
('Nicaragua'),('Niger'),('Nigeria'),('North Korea'),('North Macedonia (Macedonia)'),('Norway'),
('Oman'),('Pakistan'),('Palau'),('Palestine'),('Panama'),('Papua New Guinea'),('Paraguay'),('Peru'),
('Philippines'),('Poland'),('Portugal'),('Qatar'),('Romania'),('Russia'),('Rwanda'),('Saint Kitts & Nevis'),
('Saint Lucia'),('Saint Vincent & the Grenadines'),('Samoa'),('San Marino'),('Sao Tome & Principe'),
('Saudi Arabia'),('Senegal'),('Serbia'),('Seychelles'),('Sierra Leone'),('Singapore'),('Slovakia'),
('Slovenia'),('Solomon Islands'),('Somalia'),('South Africa'),('South Korea'),('South Sudan'),
('Spain'),('Sri Lanka'),('Sudan'),('Suriname'),('Sweden'),('Switzerland'),('Syria'),('Taiwan'),('Tajikistan'),
('Tanzania'),('Thailand'),('Timor-Leste'),('Togo'),('Tonga'),('Trinidad & Tobago'),('Tunisia'),('Turkey'),
('Turkmenistan'),('Tuvalu'),('Uganda'),('Ukraine'),('United Arab Emirates (UAE)'),('United Kingdom (UK)'),
('United States of America (USA)'),('Uruguay'),('Uzbekistan'),('Vanuatu'),('Vatican City'),('Venezuela'),
('Vietnam'),('Yemen'),('Zambia'),('Zimbabwe');


DELIMITER $$
CREATE TRIGGER after_posts_insert
AFTER INSERT ON Posts
FOR EACH ROW
BEGIN
    IF NEW.postType = 0 THEN
        INSERT INTO TextPosts (postID) VALUES (NEW.postID);
        INSERT INTO Likes (postID) VALUES (NEW.postID);
    ELSE
        INSERT INTO PicturePosts (postID) VALUES (NEW.postID);
        INSERT INTO Likes (postID) VALUES (NEW.postID);
	END IF;
END $$
DELIMITER;

DELIMITER $$$
CREATE FUNCTION insertTextPost(
  inUserID INT,
  type INT,
  intStamp BIGINT SIGNED,
  inPostText VARCHAR(145),
  inUniversalTimeStamp VARCHAR(35),
  inLocalTimeStamp VARCHAR(35)
)
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
  DECLARE returnVal boolean;
  INSERT INTO Posts (userID, postType, tStamp,universalTimeStamps,localTimeStamps)
    VALUES (inUserID,type,intStamp,inUniversalTimeStamp,inLocalTimeStamp);
  UPDATE TextPosts SET postText = inPostText
      WHERE postID = (SELECT postID FROM Posts WHERE tStamp = intStamp AND userID = inUserID);
    SET returnVal=true;
  RETURN returnVal;
END $$$
DELIMITER;


DELIMITER //
CREATE FUNCTION insertPicturePost(
  inUserID INT,
  type INT,
  intStamp BIGINT SIGNED,
  inPicture BLOB,
  inLocalTimeStamp VARCHAR(35),
  inUniversalTimeStamp VARCHAR(35)
)
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
  DECLARE returnVal boolean;
  INSERT INTO Posts (userID, postType, tStamp,universalTimeStamps,localTimeStamps)
    VALUES (inUserID,type,intStamp,inUniversalTimeStamp,inLocalTimeStamp);

  UPDATE PicturePosts SET picture = inPicture
          WHERE postID = (SELECT postID FROM Posts WHERE tStamp = intStamp AND userID = inUserID);
    SET returnVal=true;
  RETURN returnVal;
END //
DELIMITER;

--------------------------------------------------------------------------------

-- below is testing and not apart of the script

--INSERT INTO Users (userName,password,email,countryID,birthYear)
--VALUES ('TestDude','123','bla@bla.dk',1,1985);
--INSERT INTO Users (userName,password,email,countryID,birthYear) VALUES ('Thomas','123','whatever@bla.dk',1,1985);

--INSERT INTO Posts (userID, postType,tStamp,universalTimeStamps,localTimeStamps)
--VALUES (1,0,123456,'1234','1234');

--INSERT INTO TextPosts (postID,postText) VALUES (1,'whatever');



--------------------------------------------------------------------------------
-- LOG --

-- get all constraints in your db
--select * from information_schema.table_constraints where constraint_schema = 'YOUR_DB'

--ALTER TABLE tbl_name CONVERT TO CHARACTER SET charset_name;
--ALTER TABLE t1 ENGINE = InnoDB;
--ALTER TABLE Countries CONVERT TO CHARACTER SET utf8;
