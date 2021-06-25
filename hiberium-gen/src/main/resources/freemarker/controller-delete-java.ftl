
    @RequestMapping(value = "/${concept_apipath}/{id}", method = RequestMethod.DELETE)
    public Map<String, Boolean> delete${concept_name}(
            @PathVariable(value = "id") Integer ${concept_varname}Id)
            throws Exception {

        ${concept_name} ${concept_varname} = repository.getOne(${concept_varname}Id);
        if (${concept_varname} == null)
            throw new Exception("${concept_name} " + ${concept_varname}Id + " not found");

        ${concept_varname}.setFieldsForDelete();
        repository.save(${concept_varname});

        LOG.info("DELETE ${concept_name} where id=" + ${concept_varname}Id);
        Map<String, Boolean> response = new HashMap<String, Boolean>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }