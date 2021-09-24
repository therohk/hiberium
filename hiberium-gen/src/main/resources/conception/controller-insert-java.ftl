
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/${concept_apipath}", method = RequestMethod.POST)
    public ${concept_name} insert${concept_name}(
            @Valid @RequestBody ${concept_name} ${concept_varname}
            ) throws Exception {

        Integer ${concept_varname}Id = service.handle${concept_name}InsertOrUpdate(${concept_varname}, "C");
        return service.findByPrimaryKey(${concept_varname}Id);
    }