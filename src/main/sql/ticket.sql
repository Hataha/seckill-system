/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 5.6.40 : Database - ticket
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ticket` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `ticket`;

/*Table structure for table `activity` */

DROP TABLE IF EXISTS `activity`;

CREATE TABLE `activity` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `start_time` datetime NOT NULL,
  `detail` varchar(1000) DEFAULT NULL,
  `start` datetime NOT NULL,
  `rest` int(11) NOT NULL DEFAULT '9999',
  `version` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `activity_name_uindex` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

/*Data for the table `activity` */

insert  into `activity`(`id`,`name`,`start_time`,`detail`,`start`,`rest`,`version`) values 
(11,'时间未到不能抢票的活动','2021-01-31 02:00:00','TCP连接建立过程：首先Client端发送连接请求报文，Server段接受连接后回复ACK报文，并为这次连接分配资源。Client端接收到ACK报文后也向Server段发生ACK报文，并分配资源，这样TCP连接就建立了。\r\n\r\nTCP连接断开过程：假设Client端发起中断连接请求，也就是发送FIN报文。Server端接到FIN报文后，意思是说\"我Client端没有数据要发给你了\"，但是如果你还有数据没有发送完成，则不必急着关闭Socket，可以继续发送数据。所以你先发送ACK，\"告诉Client端，你的请求我收到了，但是我还没准备好，请继续你等我的消息\"。这个时候Client端就进入FIN_WAIT状态，继续等待Server端的FIN报文。当Server端确定数据已发送完成，则向Client端发送FIN报文，\"告诉Client端，好了，我这边数据发完了，准备好关闭连接了\"。Client端收到FIN报文后，\"就知道可以关闭连接了，但是他还是不相信网络，怕Server端不知道要关闭，所以发送ACK后进入TIME_WAIT状态，如果Server端没有收到ACK则可以重传。\"，Server端收到ACK后，\"就知道可以断开连接了\"。Client端等待了2MSL后依然没有收到回复，则证明Server端已正常关闭，那好，我Client端也可以关闭连接了。Ok，TCP连接就这样关闭了！','2021-02-01 02:00:00',50,0),
(12,'票抢光了的活动','2021-01-13 02:21:00','为什么要三次握手？\r\n\r\n在只有两次\"握手\"的情形下，假设Client想跟Server建立连接，但是却因为中途连接请求的数据报丢失了，故Client端不得不重新发送一遍；这个时候Server端仅收到一个连接请求，因此可以正常的建立连接。但是，有时候Client端重新发送请求不是因为数据报丢失了，而是有可能数据传输过程因为网络并发量很大在某结点被阻塞了，这种情形下Server端将先后收到2次请求，并持续等待两个Client请求向他发送数据...问题就在这里，Cient端实际上只有一次请求，而Server端却有2个响应，极端的情况可能由于Client端多次重新发送请求数据而导致Server端最后建立了N多个响应在等待，因而造成极大的资源浪费！所以，\"三次握手\"很有必要！','2021-01-16 02:25:00',0,0),
(13,'可以抢票的活动','2021-01-14 02:20:00','HTTP 中， POST 与 GET 的区别\r\n\r\n1）Get是从服务器上获取数据，Post是向服务器传送数据。\r\n2）Get是把参数数据队列加到提交表单的Action属性所指向的URL中，值和表单内各个字段一一对应，在URL中可以看到。\r\n3）Get传送的数据量小，不能大于2KB；Post传送的数据量较大，一般被默认为不受限制。\r\n4）根据HTTP规范，GET用于信息获取，而且应该是安全的和幂等的。\r\nI. 所谓 安全的 意味着该操作用于获取信息而非修改信息。换句话说，GET请求一般不应产生副作用。就是说，它仅仅是获取资源信息，就像数据库查询一样，不会修改，增加数据，不会影响资源的状态。\r\nII. 幂等 的意味着对同一URL的多个请求应该返回同样的结果。','2021-01-30 02:23:00',49,1);

/*Table structure for table `manager` */

DROP TABLE IF EXISTS `manager`;

CREATE TABLE `manager` (
  `name` varchar(20) NOT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `manager` */

insert  into `manager`(`name`,`password`) values 
('test','e10adc3949ba59abbe56e057f20f883e');

/*Table structure for table `student` */

DROP TABLE IF EXISTS `student`;

CREATE TABLE `student` (
  `id` varchar(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `password` varchar(50) NOT NULL,
  `locked` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `student` */

insert  into `student`(`id`,`name`,`password`,`locked`) values 
('1901210504','李四','e10adc3949ba59abbe56e057f20f883e',0),
('2001210400','王五','e10adc3949ba59abbe56e057f20f883e',0),
('2001210504','张三','e10adc3949ba59abbe56e057f20f883e',0);

/*Table structure for table `student_activity` */

DROP TABLE IF EXISTS `student_activity`;

CREATE TABLE `student_activity` (
  `s_id` varchar(20) NOT NULL,
  `a_id` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `student_activity` */

insert  into `student_activity`(`s_id`,`a_id`) values 
('2001210504',13);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
