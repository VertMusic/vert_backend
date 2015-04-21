SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
CREATE DATABASE VertMusicDB;
USE VertMusicDB;
CREATE TABLE IF NOT EXISTS `Comments` (
  `ID` varchar(255) NOT NULL,
  `UserID` varchar(255) NOT NULL,
  `PlaylistID` varchar(255) NOT NULL,
  `Date` varchar(50) NOT NULL,
  `Content` text NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE IF NOT EXISTS `Playlists` (
  `ID` varchar(255) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `UserID` varchar(255) NOT NULL,
  `Date` varchar(50) NOT NULL,
  `Likes` int(10) NOT NULL,
  `Visibility` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE IF NOT EXISTS `Songs` (
  `ID` varchar(255) NOT NULL,
  `Title` varchar(50) NOT NULL,
  `Artist` varchar(50) NOT NULL,
  `PlaylistID` varchar(255) NOT NULL,
  `Duration` varchar(128) NOT NULL,
  `Filepath` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE IF NOT EXISTS `Users` (
  `ID` varchar(255) NOT NULL,
  `Name` varchar(80) NOT NULL,
  `Username` varchar(80) NOT NULL,
  `Email` varchar(80) NOT NULL,
  `PasswordHash` varchar(255) NOT NULL,
  `AuthToken` varchar(255) NOT NULL,
  `Image` varchar(255) DEFAULT "",
  `ActivationCode` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;