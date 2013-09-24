package compensation.onlineshop.services;

import java.util.Date;

public class ClockImpl implements IClock{

    @Override
    public Date getTime(String reason) {
	return new Date();
    }

}
