package com.softserveinc.ita.homeproject.api.tests.houses;

import java.math.BigDecimal;
import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.HouseApi;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.Address;
import com.softserveinc.ita.homeproject.model.CreateHouse;
import com.softserveinc.ita.homeproject.model.ReadHouse;
import org.junit.jupiter.api.Test;

public class HouseApiIT {

    private final HouseApi houseApi = new HouseApi(ApiClientUtil.getClient());

//    @Test
//    void createHouseTest() throws ApiException {
//        CreateHouse createHouse = createHouse();
//        ReadHouse readHouse = houseApi.
//    }
//
//    private CreateHouse createHouse() {
//        return new CreateHouse()
//            .adjoiningArea(10)
//            .houseArea(BigDecimal.valueOf(20))
//            .quantityFlat(15)
//            .address(new Address());
//    }


}
