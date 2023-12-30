package fr.univrouen.deliverysystem.driver;

import java.util.List;

public interface DriverMapper {

	DriverEntity toDriverEntity(DriverRequest driverRequest);

	DriverRequest toDriverRequest(DriverEntity driverEntity);

	DriverResponse toDriverResponse(DriverEntity driverEntity);

	List<DriverResponse> toDriverResponseList(List<DriverEntity> driverEntities);

}
