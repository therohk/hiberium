
    @RequestMapping(value = "/${concept_apipath}", method = RequestMethod.POST)
    public ProductFaq create${concept_name}(
            @Valid @RequestBody ${concept_name} ${concept_varname}) {

        ${concept_varname}.setFieldsForCreate(authenticationFacade.getUserId());
        ProductFaq ${concept_varname}Inserted = repository.save(productFaq);
        LOG.info("INSERT ${concept_name} where id=" + ${concept_varname}Inserted.getProductFaqId());
        return ${concept_varname}Inserted;
    }