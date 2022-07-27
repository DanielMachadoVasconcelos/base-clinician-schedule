package br.com.ead.home.repositories;

import br.com.ead.home.models.Shift;
import br.com.ead.home.models.primitives.ClinicianId;

import java.util.Set;

public interface ShiftRepository {

    Set<Shift> findAllByClinicianId(ClinicianId clinicianId);

    Set<Shift> findAllShifts();
}