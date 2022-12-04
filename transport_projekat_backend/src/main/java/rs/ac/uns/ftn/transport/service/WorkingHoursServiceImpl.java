package rs.ac.uns.ftn.transport.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.WorkingHours;
import rs.ac.uns.ftn.transport.repository.WorkingHoursRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IWorkingHoursService;

@Service
public class WorkingHoursServiceImpl implements IWorkingHoursService {
    private final WorkingHoursRepository workingHoursRepository;

    public WorkingHoursServiceImpl(WorkingHoursRepository workingHoursRepository) {
        this.workingHoursRepository = workingHoursRepository;
    }

    public WorkingHours save(WorkingHours workingHours) {
        return workingHoursRepository.save(workingHours);
    }
}