package rs.ac.uns.ftn.transport.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.DriverReview;
import rs.ac.uns.ftn.transport.model.VehicleReview;
import rs.ac.uns.ftn.transport.repository.DriverRepository;
import rs.ac.uns.ftn.transport.repository.DriverReviewRepository;
import rs.ac.uns.ftn.transport.repository.VehicleRepository;
import rs.ac.uns.ftn.transport.repository.VehicleReviewRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IReviewService;

import java.util.Set;

@Service
public class ReviewServiceImpl implements IReviewService {
    private final VehicleReviewRepository vehicleReviewRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final DriverReviewRepository driverReviewRepository;

    public ReviewServiceImpl(VehicleReviewRepository vehicleReviewRepository, DriverReviewRepository driverReviewRepository,
                             VehicleRepository vehicleRepository, DriverRepository driverRepository){
        this.vehicleReviewRepository = vehicleReviewRepository;
        this.driverReviewRepository = driverReviewRepository;
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
    }

    public VehicleReview saveVehicleReview(VehicleReview review) {
        return vehicleReviewRepository.save(review);
    }

    public Set<VehicleReview> getVehicleReviewsofVehicle(Integer id){return vehicleReviewRepository.findByVehicle(vehicleRepository.findById(id).orElseGet(null));}
    public Set<DriverReview> getDriverReviewsofDriver(Integer id){return driverReviewRepository.findByDriver(driverRepository.findById(id).orElseGet(null));}

    public DriverReview saveDriverReview(DriverReview driverReview){return driverReviewRepository.save(driverReview);}
}
