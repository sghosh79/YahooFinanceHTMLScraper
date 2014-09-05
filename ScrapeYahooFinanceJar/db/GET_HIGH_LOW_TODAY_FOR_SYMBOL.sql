create or replace PROCEDURE GET_HIGH_LOW_TODAY_FOR_SYMBOL(symbol in varchar2, outVar out sys_refcursor) AS 
BEGIN
 OPEN outVar FOR
              select * from(
                select DT, DAY_OPEN, DAY_LOW, DAY_HIGH, DAY_CLOSE from 
                        (select * from 
                                (select * from stock_daily_history s where s.stock_key = (select stock_key from master_stock where stock_symbol = symbol)  order by s.dt desc) 
                        where rownum <= 252 ) 
                where 
                (day_low = (select min(day_low) from 
                                        (select * from 
                                                (select * from stock_daily_history s where s.stock_key = (select stock_key from master_stock where stock_symbol = symbol) 
                                        order by s.dt desc) 
                                where rownum <= 252)))
                union
                
                select DT, DAY_OPEN, DAY_LOW, DAY_HIGH, DAY_CLOSE from 
                        (select * from 
                                (select * from stock_daily_history s where s.stock_key = (select stock_key from master_stock where stock_symbol = symbol)  order by s.dt desc) 
                        where rownum <= 252 ) 
                where 
                (day_high = (select max(day_high) from 
                                        (select * from 
                                                (select * from stock_daily_history s where s.stock_key = (select stock_key from master_stock where stock_symbol = symbol) 
                                        order by s.dt desc) 
                                where rownum <= 252)))
                union 
                                                       
                select DT, DAY_OPEN, DAY_LOW, DAY_HIGH, DAY_CLOSE from 
                        (select * from stock_daily_history s where s.stock_key = (select stock_key from master_stock where stock_symbol = symbol)  order by s.dt desc) 
                where rownum <= 1
                ) 
        order by day_close asc;
END GET_HIGH_LOW_TODAY_FOR_SYMBOL;