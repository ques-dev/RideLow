package rs.ac.uns.ftn.transport.service.interfaces;

import rs.ac.uns.ftn.transport.model.Vehicle;
import rs.ac.uns.ftn.transport.model.VehicleReview;

import java.util.Set;

public interface IReviewService {
    VehicleReview saveVehicleReview(VehicleReview review);
    Set<VehicleReview> getVehicleReviewsofVehicle(Integer id);
}
