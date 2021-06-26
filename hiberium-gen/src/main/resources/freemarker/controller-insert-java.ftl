
    @RequestMapping(value = "/${concept_apipath}", method = RequestMethod.POST)
    public ${concept_name} create${concept_name}(
            @Valid @RequestBody ${concept_name} ${concept_varname})
            throws Exception {

        ${concept_varname}.handleFieldsForInsert();
        ${concept_name} ${concept_varname}Inserted = repository.save(${concept_varname});
        log.info("INSERT ${concept_name} where id=" + ${concept_varname}Inserted.getPrimaryKey());
        return ${concept_varname}Inserted;
    }