FROM gradle:jdk21-corretto-al2023 AS build

WORKDIR ktnative-build
ADD ktnative/ .
RUN gradle dependencies
RUN gradle packageLambda

FROM public.ecr.aws/lambda/provided:al2023
#RUN dnf install
WORKDIR /function

COPY --from=build /ktnative-build/build/bin/linuxX64/releaseExecutable/ktnative.kexe .
ENTRYPOINT ["/function/ktnative.kexe"]
# anything
#CMD ["ktnativeHandler"]
