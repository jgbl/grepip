-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 26. Jun 2016 um 17:29
-- Server Version: 5.5.47-0ubuntu0.14.04.1
-- PHP-Version: 5.5.9-1ubuntu4.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `ips`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ip`
--

CREATE TABLE IF NOT EXISTS `ip` (
  `ID` int(10) unsigned NOT NULL,
  `address` varchar(20) NOT NULL,
  `count` int(10) unsigned NOT NULL DEFAULT '0',
  `kind` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `address` (`address`),
  KEY `kind` (`kind`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `kinds`
--

CREATE TABLE IF NOT EXISTS `kinds` (
  `ID` int(10) unsigned NOT NULL,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `kinds`
--

INSERT INTO `kinds` (`ID`, `name`) VALUES
(32, 'critical'),
(16, 'drop'),
(1, 'extern'),
(2, 'local'),
(8, 'multi'),
(4, 'multi local');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `text`
--

CREATE TABLE IF NOT EXISTS `text` (
  `ipID` int(10) unsigned NOT NULL,
  `text` text NOT NULL,
  KEY `ipID` (`ipID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
