package rs.ac.uns.ftn.transport.service.interfaces;

import rs.ac.uns.ftn.transport.model.WorkingHours;

public interface IWorkingHoursService {
    WorkingHours save(WorkingHours workingHours);

    WorkingHours findOne(Integer id);
}