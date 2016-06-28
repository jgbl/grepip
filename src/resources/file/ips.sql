-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 28, 2016 at 07:47 PM
-- Server version: 5.5.49-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `ips`
--
CREATE DATABASE IF NOT EXISTS `ips` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `ips`;

-- --------------------------------------------------------

--
-- Table structure for table `ip`
--

CREATE TABLE IF NOT EXISTS `ip` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `address` varchar(50) NOT NULL,
  `count` int(10) unsigned NOT NULL DEFAULT '0',
  `kind` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `address` (`address`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `ip`
--

INSERT INTO `ip` (`ID`, `address`, `count`, `kind`) VALUES
(1, '40.86.89.181', 16, 0),
(2, '192.168.178.22', 15, 0);

-- --------------------------------------------------------

--
-- Table structure for table `kinds`
--

CREATE TABLE IF NOT EXISTS `kinds` (
  `ID` int(10) unsigned NOT NULL,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `kinds`
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
-- Table structure for table `text`
--

CREATE TABLE IF NOT EXISTS `text` (
  `ipID` int(10) unsigned NOT NULL,
  `text` text NOT NULL,
  KEY `ipID` (`ipID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `text`
--

INSERT INTO `text` (`ipID`, `text`) VALUES
(1, '      1 0.000000       40.86.89.181          192.168.178.22        TCP      54     443 ? 56896 [ACK] Seq=1 Ack=1 Win=23040 Len=0'),
(1, '      1 0.000000       40.86.89.181          192.168.178.22        TCP      54     443 ? 56896 [ACK] Seq=1 Ack=1 Win=23040 Len=0'),
(2, '      1 0.000000       40.86.89.181          192.168.178.22        TCP      54     443 ? 56896 [ACK] Seq=1 Ack=1 Win=23040 Len=0'),
(1, 'Internet Protocol Version 4, Src: 40.86.89.181, Dst: 192.168.178.22'),
(2, 'Internet Protocol Version 4, Src: 40.86.89.181, Dst: 192.168.178.22'),
(2, '      2 0.000199       192.168.178.22        40.86.89.181          TLSv1.2  139    Application Data'),
(1, '      2 0.000199       192.168.178.22        40.86.89.181          TLSv1.2  139    Application Data'),
(2, 'Internet Protocol Version 4, Src: 192.168.178.22, Dst: 40.86.89.181'),
(1, 'Internet Protocol Version 4, Src: 192.168.178.22, Dst: 40.86.89.181'),
(1, '      3 0.000555       40.86.89.181          192.168.178.22        TCP      54     [TCP ACKed unseen segment] 443 ? 56896 [ACK] Seq=1 Ack=102 Win=23040 Len=0'),
(2, '      3 0.000555       40.86.89.181          192.168.178.22        TCP      54     [TCP ACKed unseen segment] 443 ? 56896 [ACK] Seq=1 Ack=102 Win=23040 Len=0'),
(1, 'Internet Protocol Version 4, Src: 40.86.89.181, Dst: 192.168.178.22'),
(2, 'Internet Protocol Version 4, Src: 40.86.89.181, Dst: 192.168.178.22'),
(2, '      4 0.000923       192.168.178.22        40.86.89.181          TLSv1.2  155    Application Data'),
(1, '      4 0.000923       192.168.178.22        40.86.89.181          TLSv1.2  155    Application Data'),
(2, 'Internet Protocol Version 4, Src: 192.168.178.22, Dst: 40.86.89.181'),
(1, 'Internet Protocol Version 4, Src: 192.168.178.22, Dst: 40.86.89.181'),
(1, '      5 0.152977       40.86.89.181          192.168.178.22        TLSv1.2  155    Application Data'),
(2, '      5 0.152977       40.86.89.181          192.168.178.22        TLSv1.2  155    Application Data'),
(1, 'Internet Protocol Version 4, Src: 40.86.89.181, Dst: 192.168.178.22'),
(2, 'Internet Protocol Version 4, Src: 40.86.89.181, Dst: 192.168.178.22'),
(2, '      6 0.153607       192.168.178.22        40.86.89.181          TCP      54     56896 ? 443 [ACK] Seq=102 Ack=102 Win=6160 Len=0'),
(1, '      6 0.153607       192.168.178.22        40.86.89.181          TCP      54     56896 ? 443 [ACK] Seq=102 Ack=102 Win=6160 Len=0'),
(2, 'Internet Protocol Version 4, Src: 192.168.178.22, Dst: 40.86.89.181'),
(1, 'Internet Protocol Version 4, Src: 192.168.178.22, Dst: 40.86.89.181'),
(2, '      7 0.400112       192.168.178.22        40.86.89.181          TCP      54     [TCP Window Update] 56896 ? 443 [ACK] Seq=102 Ack=102 Win=6183 Len=0'),
(1, '      7 0.400112       192.168.178.22        40.86.89.181          TCP      54     [TCP Window Update] 56896 ? 443 [ACK] Seq=102 Ack=102 Win=6183 Len=0'),
(2, 'Internet Protocol Version 4, Src: 192.168.178.22, Dst: 40.86.89.181'),
(1, 'Internet Protocol Version 4, Src: 192.168.178.22, Dst: 40.86.89.181');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `text`
--
ALTER TABLE `text`
  ADD CONSTRAINT `fk_text_ip` FOREIGN KEY (`ipID`) REFERENCES `ip` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
