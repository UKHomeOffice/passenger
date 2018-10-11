package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class CrsController {

    @Autowired
    private CrsRecordRepository crsRecordRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/crsrecords", method = RequestMethod.GET)
    public String viewCrsRecordsDetails(Map<String, Object> model) {
        if(model.get("crsRecords") == null && (model.get("fileUploadError") == null)){
            model.put("crsRecords", crsRecordRepository.getAll().orElse(null));
        }
        return "crsrecords/crsrecords";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/deletecrsrecord/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable("id") String id) {
       crsRecordRepository.deleteById(Long.valueOf(id));
       return "redirect:/crsrecords";
    }

}

