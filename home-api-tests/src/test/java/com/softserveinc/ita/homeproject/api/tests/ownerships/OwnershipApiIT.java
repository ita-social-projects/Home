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

    private static final long testOwnershipId = 10000001L;

    private static final Long testDeleteOwnershipId = 10000003L;

    private static final long testApartmentId = 100000000L;

    private static final long testDeleteOwnershipApartmentId = 100000001L;

    @Test
    void getApartmentTest() throws ApiException {

        ReadOwnership expectedOwnership = ownershipApi.getOwnership(testApartmentId, testOwnershipId);

        ApiResponse<ReadOwnership> response
                = ownershipApi.getOwnershipWithHttpInfo(testApartmentId, testOwnershipId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertApartment(expectedOwnership, response.getData());
    }

    @Test
    void getNonExistentOwnership() {
        Long wrongId = 20000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi
                        .getOwnershipWithHttpInfo(testApartmentId, wrongId))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Ownership with 'id: " + wrongId + "' is not found");
    }

    @Test
    void getOwnershipWithNonExistentApartment() {
        Long wrongId = 20000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi
                        .getOwnershipWithHttpInfo(wrongId, testOwnershipId))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Ownership with 'id: " + testOwnershipId + "' is not found");
    }

    @Test
    void updateOwnershipTest() throws ApiException {
        UpdateOwnership updateOwnership = new UpdateOwnership()
                .ownershipPart(BigDecimal.valueOf(0.5));

        ReadOwnership expectedOwnership = ownershipApi.getOwnership(testApartmentId, testOwnershipId);

        ApiResponse<ReadOwnership> response =
                ownershipApi.updateOwnershipWithHttpInfo(testApartmentId, testOwnershipId, updateOwnership);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertOwnership(expectedOwnership, updateOwnership, response.getData());
    }

    @Test
    void updateOwnershipWithInvalidOwnershipPart() throws ApiException {
        UpdateOwnership updateOwnership = new UpdateOwnership()
                .ownershipPart(BigDecimal.valueOf(0.8));

        ReadOwnership expectedOwnership = ownershipApi.getOwnership(testApartmentId, testOwnershipId);

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi.updateOwnership(testApartmentId,expectedOwnership.getId(), updateOwnership))
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
                        .updateOwnershipWithHttpInfo(testApartmentId, wrongId, updateOwnership))
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
                        .updateOwnershipWithHttpInfo(wrongId, testOwnershipId, updateOwnership))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Apartment with 'id: " + wrongId + "' is not found");
    }

    @Test
    void deleteApartmentTest() throws ApiException {

        ApiResponse<Void> response = ownershipApi.deleteOwnershipWithHttpInfo(testDeleteOwnershipApartmentId, testDeleteOwnershipId);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatusCode());
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi.getOwnership(testDeleteOwnershipApartmentId, testDeleteOwnershipId));

    }

    @Test
    void deleteOwnershipWithNonExistentApartment() {

        Long wrongId = 2000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> ownershipApi
                        .deleteOwnershipWithHttpInfo(wrongId, testOwnershipId))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Apartment with 'id: " + wrongId +"' is not found");
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
