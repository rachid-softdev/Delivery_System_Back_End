package fr.univrouen.deliverysystem.round;

import fr.univrouen.deliverysystem.delivery.DeliveryResponse;
import fr.univrouen.deliverysystem.driver.DriverResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RoundRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String publicId;

  @NotBlank(message = "Le champ name est requis.")
  @Size(
    min = 2,
    max = 50,
    message = "Le aaa nom de la tournée doit contenir entre {min} et {max} caractères."
  )
  private String name;

  @NotNull(message = "Le champ startDate est requis.")
  private Instant startDate;

  @NotNull(message = "Le champ endDate est requis.")
  private Instant endDate;

  private DriverResponse driver;

  private List<DeliveryResponse> deliveries;

  public RoundRequest() {
    this.publicId = "";
    this.name = "";
    this.startDate = this.endDate = Instant.now();
    this.driver = null;
    this.deliveries = new ArrayList<DeliveryResponse>();
  }

  public RoundRequest(
    String publicId,
    String name,
    Instant startDate,
    Instant endDate,
    DriverResponse driver,
    List<DeliveryResponse> deliveries
  ) {
    this.publicId = publicId;
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.driver = driver;
    this.deliveries = deliveries;
  }

  public String getPublicId() {
    return publicId;
  }

  public void setPublicId(String publicId) {
    this.publicId = publicId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Instant getStartDate() {
    return startDate;
  }

  public void setStartDate(Instant startDate) {
    this.startDate = startDate;
  }

  public Instant getEndDate() {
    return endDate;
  }

  public void setEndDate(Instant endDate) {
    this.endDate = endDate;
  }

  public DriverResponse getDriver() {
    return this.driver;
  }

  public void setDriver(DriverResponse driver) {
    this.driver = driver;
  }

  public List<DeliveryResponse> getDeliveries() {
    return deliveries;
  }

  public void setDeliveries(List<DeliveryResponse> deliveries) {
    this.deliveries = deliveries;
  }

  @Override
  public String toString() {
    return (
      "RoundRequest[" +
      "publicId='" +
      publicId +
      '\'' +
      ", name='" +
      name +
      '\'' +
      ", startDate=" +
      startDate +
      ", endDate=" +
      endDate +
      "]"
    );
  }

  public static RoundRequestBuilder builder() {
    return new RoundRequestBuilder();
  }

  public static class RoundRequestBuilder {

    private String publicId;
    private String name;
    private Instant startDate;
    private Instant endDate;
    private DriverResponse driver;
    private List<DeliveryResponse> deliveries;

    public RoundRequestBuilder publicId(String publicId) {
      this.publicId = publicId;
      return this;
    }

    public RoundRequestBuilder name(String name) {
      this.name = name;
      return this;
    }

    public RoundRequestBuilder startDate(Instant startDate) {
      this.startDate = startDate;
      return this;
    }

    public RoundRequestBuilder endDate(Instant endDate) {
      this.endDate = endDate;
      return this;
    }

    public RoundRequestBuilder driver(DriverResponse driver) {
      this.driver = driver;
      return this;
    }

    public RoundRequestBuilder deliveries(List<DeliveryResponse> deliveries) {
      this.deliveries = deliveries;
      return this;
    }

    public RoundRequest build() {
      RoundRequest roundRequest = new RoundRequest();
      roundRequest.setPublicId(this.publicId);
      roundRequest.setName(this.name);
      roundRequest.setStartDate(this.startDate);
      roundRequest.setEndDate(this.endDate);
      roundRequest.setDriver(this.driver);
      roundRequest.setDeliveries(this.deliveries);
      return roundRequest;
    }
  }
}
