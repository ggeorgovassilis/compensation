package org.github.ggeorgovassilis.compensation.onlineshop.services;

import java.util.Date;

public interface IClock {

    Date getTime(String reason);
}
