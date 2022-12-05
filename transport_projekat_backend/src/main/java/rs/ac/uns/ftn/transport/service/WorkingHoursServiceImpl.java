package rs.ac.uns.ftn.transport.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.WorkingHours;
import rs.ac.uns.ftn.transport.repository.WorkingHoursRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IWorkingHoursService;

import java.time.LocalDateTime;

@Service
public class WorkingHoursServiceImpl implements IWorkingHoursService {
    private final WorkingHoursRepository workingHoursRepository;

    public WorkingHoursServiceImpl(WorkingHoursRepository workingHoursRepository) {
        this.workingHoursRepository = workingHoursRepository;
    }

    public WorkingHours save(WorkingHours workingHours) {
        return workingHoursRepository.save(workingHours);
    }

    public WorkingHours findOne(Integer id) {
        return workingHoursRepository.findById(id).orElseGet(null);
    }

    public Page<WorkingHours> findAllByDriver_Id(Integer id, Pageable page) {
        return workingHoursRepository.findAllByDriver_Id(id, page);
    }

    public Page<WorkingHours> findAllByDriver_IdAndStartIsAfterAndEndIsBefore(Integer id, LocalDateTime start, LocalDateTime end, Pageable page) {
        return workingHoursRepository.findAllByDriver_IdAndStartIsAfterAndEndIsBefore(id, start, end, page);
    }

    public Page<WorkingHours> findAllByDriver_IdAndStartIsAfter(Integer id, LocalDateTime start, Pageable page) {
        return workingHoursRepository.findAllByDriver_IdAndStartIsAfter(id, start, page);
    }

    public Page<WorkingHours> findAllByDriver_IdAndEndIsBefore(Integer id, LocalDateTime end, Pageable page) {
        return workingHoursRepository.findAllByDriver_IdAndEndIsBefore(id, end, page);
    }
}