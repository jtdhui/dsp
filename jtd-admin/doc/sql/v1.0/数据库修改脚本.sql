set global log_bin_trust_function_creators=1;
drop function if exists queryPartnerChildren;

delimiter $$
CREATE DEFINER=`root`@`%` FUNCTION `queryPartnerChildren`(partnerId INT) RETURNS varchar(4000) CHARSET utf8
  BEGIN
    DECLARE sTemp varchar(4000) ;
    DECLARE sTempChd varchar(4000);
    SET sTemp = '$';
    SET sTempChd =cast(partnerId as CHAR);

    WHILE sTempChd is not null DO
      SET sTemp = concat(sTemp,',',sTempChd);
      SELECT group_concat(id) INTO sTempChd FROM partner where FIND_IN_SET(pid,sTempChd)>0;
    END WHILE;
    RETURN sTemp;

  END $$

delimiter ;

set global log_bin_trust_function_creators=0;