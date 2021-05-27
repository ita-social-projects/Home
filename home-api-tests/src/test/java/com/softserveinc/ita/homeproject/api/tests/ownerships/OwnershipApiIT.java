package com.softserveinc.ita.homeproject.api.tests.ownerships;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ApiResponse;
import com.softserveinc.ita.homeproject.api.ApartmentOwnershipApi;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.ReadOwnership;
import com.softserveinc.ita.homeproject.model.UpdateOwnership;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.BAD_REQUEST;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OwnershipApiIT {

    private final ApartmentOwnershipApi ownershipApi = new ApartmentOwnershipApi(ApiClientUtil.getClient());

    private static final long TEST_OWNERSHIP_ID = 10000001L;

    private static final long TEST_DELETE_OWNERSHIP_ID = 10000003L;

    private static final long TEST_APARTMENT_ID = 100000000L;

    private static final long TEST_DELETE_OWNERSHIP_APARTMENT_ID = 100000001L;

    @Test
    void getOwnershipTest() throws ApiException {

        ReadOwnership expectedOwnership = ownershipApi.getOwnership(TEST_APARTMENT_ID, TEST_OWNERSHIP_ID);

        ApiResponse<ReadOwnership> response
                = ownershipApi.getOwnershipWithHttpInfo(TEST_APARTMENT_ID, TEST_OWNERSHIP_ID);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertApartment(expectedOwnership, response.getData());
    }

    @Test
    void getNonExistentOwnership() {
        Long wrongId = 20000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi
                        .getOwnershipWithHttpInfo(TEST_APARTMENT_ID, wrongId))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Ownership with 'id: " + wrongId + "' is not found");
    }

    @Test
    void getOwnershipWithNonExistentApartment() {
        Long wrongId = 20000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi
                        .getOwnershipWithHttpInfo(wrongId, TEST_OWNERSHIP_ID))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Ownership with 'id: " + TEST_OWNERSHIP_ID + "' is not found");
    }

    @Test
    void updateOwnershipTest() throws ApiException {
        UpdateOwnership updateOwnership = new UpdateOwnership()
                .ownershipPart(BigDecimal.valueOf(0.5));

        ReadOwnership expectedOwnership = ownershipApi.getOwnership(TEST_APARTMENT_ID, TEST_OWNERSHIP_ID);

        ApiResponse<ReadOwnership> response =
                ownershipApi.updateOwnershipWithHttpInfo(TEST_APARTMENT_ID, TEST_OWNERSHIP_ID, updateOwnership);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertOwnership(expectedOwnership, updateOwnership, response.getData());
    }

    @Test
    void updateOwnershipWithInvalidOwnershipPart() throws ApiException {
        UpdateOwnership updateOwnership = new UpdateOwnership()
                .ownershipPart(BigDecimal.valueOf(0.8));

        ReadOwnership expectedOwnership = ownershipApi.getOwnership(TEST_APARTMENT_ID, TEST_OWNERSHIP_ID);

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi.updateOwnership(TEST_APARTMENT_ID,expectedOwnership.getId(), updateOwnership))
                .matches((actual) -> actual.getCode() == BAD_REQUEST)
                .withMessageContaining("Entered sum of area = 1.2 The sum of the entered area cannot be greater than 1");
    }

    @Test
    void updateNonExistentOwnershipTest() {
        UpdateOwnership updateOwnership = new UpdateOwnership()
                .ownershipPart(BigDecimal.valueOf(0.5));

        Long wrongId = 2000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi
                        .updateOwnershipWithHttpInfo(TEST_APARTMENT_ID, wrongId, updateOwnership))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Ownership with 'id: " + wrongId + "' is not found");
    }

    @Test
    void updateOwnershipWithNonExistentApartmentTest() {
        UpdateOwnership updateOwnership = new UpdateOwnership()
                .ownershipPart(BigDecimal.valueOf(0.5));

        Long wrongId = 2000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi
                        .updateOwnershipWithHttpInfo(wrongId, TEST_OWNERSHIP_ID, updateOwnership))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Ownership with 'id: " + TEST_OWNERSHIP_ID + "' is not found");
    }

    @Test
    void deleteOwnershipTest() throws ApiException {

        ApiResponse<Void> response = ownershipApi.deleteOwnershipWithHttpInfo(TEST_DELETE_OWNERSHIP_APARTMENT_ID, TEST_DELETE_OWNERSHIP_ID);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatusCode());
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi.getOwnership(TEST_DELETE_OWNERSHIP_APARTMENT_ID, TEST_DELETE_OWNERSHIP_ID));

    }

    @Test
    void deleteOwnershipWithNonExistentApartment() {

        Long wrongId = 2000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi
                        .deleteOwnershipWithHttpInfo(wrongId, TEST_OWNERSHIP_ID))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Ownership with 'id: " + TEST_OWNERSHIP_ID +"' is not found");
    }

    private void assertApartment(ReadOwnership expected, ReadOwnership actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getOwnershipPart(), actual.getOwnershipPart());
    }

    private void assertOwnership(ReadOwnership saved, UpdateOwnership update, ReadOwnership updated) {
        assertNotNull(saved);
        assertNotNull(update);
        assertNotNull(updated);
        assertEquals(update.getOwnershipPart(), updated.getOwnershipPart());
    }
}
